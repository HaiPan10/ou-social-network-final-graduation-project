package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Role implements Serializable {

    private Long id;

    @NotNull(message = "{role.name.notNull}")
    @Size(min = 1, max = 45, message = "{role.name.invalidSize}")
    private String name;

    @JsonIgnore
    private List<Account> accountList;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", name=" + name + "]";
    }
    
}
