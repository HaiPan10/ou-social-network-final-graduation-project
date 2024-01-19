// // Scripts for firebase and firebase messaging
// importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
// importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

// // Initialize the Firebase app in the service worker by passing the generated config
// const firebaseConfig = {
//   apiKey: "AIzaSyAUvwNlEKrz7fp8oC1JJldlL29vLrpSwTM",
//   authDomain: "ou-social-network-bf1ea.firebaseapp.com",
//   projectId: "ou-social-network-bf1ea",
//   storageBucket: "ou-social-network-bf1ea.appspot.com",
//   messagingSenderId: "587365434814",
//   appId: "1:587365434814:web:1af6baafe45803c108a5ef",
//   measurementId: "G-01G2X972TF"
// };

// firebase.initializeApp(firebaseConfig);

// const messaging = firebase.messaging();

// messaging.onBackgroundMessage(function(payload) {
//   console.log('Received background message ', payload);

//   const notificationTitle = payload.notification.title;
//   const notificationOptions = {
//     body: payload.notification.body,
//   };

//   self.registration.showNotification(notificationTitle,
//     notificationOptions);
// });
