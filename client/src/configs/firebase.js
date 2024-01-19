import { initializeApp } from 'firebase/app';
import { getAuth} from 'firebase/auth';
import { getFirestore } from 'firebase/firestore'

const firebaseConfig = {
    apiKey: "AIzaSyAUvwNlEKrz7fp8oC1JJldlL29vLrpSwTM",
    authDomain: "ou-social-network-bf1ea.firebaseapp.com",
    projectId: "ou-social-network-bf1ea",
    storageBucket: "ou-social-network-bf1ea.appspot.com",
    messagingSenderId: "587365434814",
    appId: "1:587365434814:web:1af6baafe45803c108a5ef",
    measurementId: "G-01G2X972TF"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getFirestore(app);