package com.ou.accountservice.repository.repositoryJPA;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ou.accountservice.pojo.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryJPA extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    Optional<Account> findById(Long id);

    @Query("SELECT a " +
           "FROM Account a " +
           "JOIN User u ON a.id = u.id " +
           "WHERE LOWER(CONCAT(a.id, a.email, u.lastName, ' ', u.firstName)) LIKE %?1% AND " +
           "a.status LIKE %?2%")
    List<Account> search(String kw, String status, Pageable pageable);

    @Query("SELECT COUNT(a.id) " +
           "FROM Account a " +
           "JOIN User u ON a.id = u.id " +
           "WHERE LOWER(CONCAT(a.id, a.email, u.lastName, ' ', u.firstName)) LIKE %?1% AND " +
           "a.status LIKE %?2%")
    Long countAccounts(String kw, String status);

    @Query("SELECT a " +
           "FROM Account a " +
           "WHERE a.status = 'AUTHENTICATION_PENDING'")
    List<Account> getPendingAccounts(Pageable pageable);

    @Query("SELECT COUNT(a) FROM Account a WHERE a.status = 'AUTHENTICATION_PENDING'")
    Long countPendingAccounts();

    @Modifying
    @Query("UPDATE Account a SET a.status = ?2 WHERE a.id = ?1")
    void verifyAccount(Long accountId, String status);
    
    @Query("SELECT a.status FROM Account a WHERE a.id = ?1")
    String getStatus(Long accountId);

    @Query("SELECT a.id, a.email, CONCAT(u.lastName, ' ', u.firstName), u.avatar " +
           "FROM Account a " +
           "JOIN User u ON a.id = u.id " +
           "WHERE a.status = 'ACTIVE' OR a.status = 'PASSWORD_CHANGE_REQUIRED'")
    List<Object[]> list();

    @Query("SELECT " +
           "CASE WHEN :byMonth = true THEN MONTH(a.createdDate) " +
           "     WHEN :byQuarter = true THEN QUARTER(a.createdDate) " +
           "     ELSE YEAR(a.createdDate) END, " +
           "COUNT(a.id) " +
           "FROM Account a " +
           "WHERE (:year is null OR YEAR(a.createdDate) = :year) " +
           "GROUP BY " +
           "CASE WHEN :byMonth = true THEN MONTH(a.createdDate) " +
           "     WHEN :byQuarter = true THEN QUARTER(a.createdDate) " +
           "     ELSE YEAR(a.createdDate) END")
    List<Object[]> stat(@Param("year") Integer year,
                        @Param("byMonth") boolean byMonth,
                        @Param("byQuarter") boolean byQuarter);
}
