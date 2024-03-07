// package com.ou.accountservice.service.impl;

// import java.io.IOException;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.env.Environment;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.multipart.MultipartFile;

// import com.ou.accountservice.pojo.UserDoc;
// import com.ou.accountservice.pojo.Account;
// import com.ou.accountservice.pojo.User;
// import com.ou.accountservice.repository.repositoryJPA.UserRepositoryJPA;
// import com.ou.accountservice.service.interfaces.AccountService;
// import com.ou.accountservice.service.interfaces.CloudinaryService;
// // import com.ou.accountservice.service.interfaces.FirebaseService;
// // import com.ou.accountservice.service.interfaces.PostService;
// import com.ou.accountservice.service.interfaces.UserService;


// @Service
// @Transactional(rollbackFor = Exception.class)
// public class UserServiceImpl implements UserService {    
//     @Autowired
//     private Environment env;
//     @Autowired
//     private AccountService accountService;;
//     @Autowired
//     private UserRepositoryJPA userRepositoryJPA;
//     @Autowired
//     private CloudinaryService cloudinaryService;
//     // @Autowired
//     // private FirebaseService firebaseService;
//     // @Autowired
//     // private PostService postService

//     @Override
//     public User create(User user) {
//         return userRepositoryJPA.save(user);
//     }

//     @Override
//     public User uploadAvatar(MultipartFile uploadAvatar, Long userId) throws Exception {
//         try {
//             String newUrl = cloudinaryService.uploadImage(uploadAvatar);
//             User persistUser = retrieve(userId);
//             String oldUrl = persistUser.getAvatar();
//             persistUser.setAvatar(newUrl);
//             // User returnUser = userRepository.updateAvatar(persistUser, newUrl);
//             User returnUser = userRepositoryJPA.save(persistUser);
//             String defaultAvatar = this.env.getProperty("DEFAULT_AVATAR").toString();
//             if (oldUrl != null && !oldUrl.equals(defaultAvatar)) {
//                 cloudinaryService.deleteImage(oldUrl);
//             }
//             UserDoc userDoc = firebaseService.retrieve(userId.toString());
//             userDoc.setPhotoUrl(newUrl);
//             firebaseService.saveOrUpdate(userDoc);
//             return returnUser;
//         } catch (IOException e) {
//             throw new IOException("Fail to upload avatar");
//         }
//     }

//     @Override
//     public User uploadCover(MultipartFile uploadCover, Long userId) throws Exception {
//         try {
//             String newUrl = cloudinaryService.uploadImage(uploadCover);
//             User persistUser = retrieve(userId);
//             String oldUrl = persistUser.getCoverAvatar();
//             String defaultCover = this.env.getProperty("DEFAULT_COVER").toString();
//             // User returnUser = userRepository.updateCover(persistUser, newUrl);
//             persistUser.setCoverAvatar(newUrl);
//             User returnUser = userRepositoryJPA.save(persistUser);
//             if (!oldUrl.equals(defaultCover)) {
//                 cloudinaryService.deleteImage(oldUrl);
//             }
//             return returnUser;
//         } catch (IOException e) {
//             throw new IOException("Fail to upload avatar");
//         }
//     }

//     @Override
//     public User retrieve(Long userId) throws Exception {
//         // Optional<User> userOptional = userRepository.retrieve(userId);
//         Optional<User> userOptional = userRepositoryJPA.findById(userId);
//         if (userOptional.isPresent()) {
//             return userOptional.get();
//         } else {
//             throw new Exception("Không tìm thấy người dùng");
//         }
//     }

//     @Override
//     public Map<String, Object> loadProfile(Long userId, Long currentUserId, Map<String, String> params) throws Exception {
//         System.out.println("IN SERVICE");
//         Account retrieveAccount = accountService.retrieve(userId);
//         if (!(retrieveAccount.getStatus().equals("ACTIVE") || retrieveAccount.getStatus().equals("PASSWORD_CHANGE_REQUIRED"))) {
//             throw new Exception("Not activated Account!");
//         }
//         Map<String, Object> jsonObject = new HashMap<>();
//         jsonObject.put("user", retrieveAccount.getUser());
//         jsonObject.put("role", retrieveAccount.getRoleId());
//         System.out.println("RETRIEVING POSTS");
//         jsonObject.put("posts", postService.loadPost(userId, currentUserId, params));
//         return jsonObject;
//     }

//     @Override
//     public User updateUser(User user, Long userId) {
//         User persistentUser = userRepositoryJPA.findById(userId).orElse(null);
//         if(user.getDob() != null){
//             persistentUser.setDob(user.getDob());
//         }

//         if(user.getFirstName() != null){
//             persistentUser.setFirstName(user.getFirstName());
//         }

//         if(user.getLastName() != null){
//             persistentUser.setLastName(user.getLastName());
//         }

//         // return userRepository.updateUser(user, userId);
//         return userRepositoryJPA.save(persistentUser);
//     }

//     @Override
//     public List<User> list(List<Long> listUserId) {
//         return userRepositoryJPA.list(listUserId);
//     }

//     @Override
//     public List<User> list(){
//         return userRepositoryJPA.findAll();
//     }
    
// }
