import { defineStore } from "pinia";
import { useMessageStore } from "../stores/message";
import axios from "axios";

const user = JSON.parse(localStorage.getItem("user"));

export const useAuthStore = defineStore({
  id: "auth",
  state: () => ({
    loggedIn: user ? true : false,
    user: user ? user : null,
  }),
  getters: {
    // doubleCount: (state) => state.user * 2,
  },
  actions: {
    async login(user) {
      const messageStore = useMessageStore();
      try {
        const res = await axios.post(
          import.meta.env.VITE_API_URL + "auth/signin",
          {
            username: user.username,
            password: user.password,
          }
        );
        localStorage.setItem("user", JSON.stringify(res.data));
        this.loggedIn = true;
        this.user = res.data;
        messageStore.success = "You have been susseccfully logged in !";
      } catch (error) {
        console.log(error);
        if (error.response.status == 403) {
          messageStore.error = "Wrong username or password !";
        } else {
          messageStore.error = error.message;
        }
      }
    },
    async register(user) {
      const messageStore = useMessageStore();
      try {
        const res = await axios.post(
          import.meta.env.VITE_API_URL + "auth/signup",
          user
        );
        messageStore.success = res.data;
      } catch (error) {
        console.log(error);
        messageStore.error = error.response.data.message;
      }
    },
    async logout() {
      const messageStore = useMessageStore();
      this.loggedIn = false;
      this.user = null;
      localStorage.removeItem("user");
      messageStore.success = "You have been susseccfully logged out !";
    },
  },
});
