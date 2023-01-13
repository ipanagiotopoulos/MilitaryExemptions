import { defineStore } from "pinia";

export const useMessageStore = defineStore({
  id: "message",
  state: () => ({
    error: "",
    success: "",
  }),
  getters: {},
  actions: {},
});
