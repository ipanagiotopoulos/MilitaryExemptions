import { defineStore } from "pinia";
import { useAuthStore } from "../stores/auth";
import axios from "axios";
import { useMessageStore } from "../stores/message";

export const useApplicationStore = defineStore({
  id: "animal",
  state: () => ({
    createdApplication: {},
    updatedApplication: {},
    currentApplication: {},
    applications: [],
    error: "",
    success: "",
  }),
  getters: {},
  actions: {
    async createApplication(application, file) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      application.citizenUsername = authStore.user.username;
      application.milNumber = Number(application.milNumber);
      console.log(authStore.user.username);
      try {
        const res = await axios.post(
          import.meta.env.VITE_API_URL + "applications",
          application,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        await this.saveApplicationFile(res.data.id, file);
        this.createdApplication = res.data;
        messageStore.success = "Application saved !";
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async saveApplicationFile(id, file) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      const body = new FormData();
      body.append("file", file);
      try {
        const res = await axios.post(
          import.meta.env.VITE_API_URL + "applications/" + id + "/file",
          body,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
              "content-type": "multipart/form-data",
            },
          }
        );

        messageStore.success = res.data;
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async updateApplication(id, application, file) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        await axios.put(
          import.meta.env.VITE_API_URL + "applications/" + id,
          application,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        if (file != null) {
          await this.saveApplicationFile(id, file);
        }
        messageStore.success = "Application updated !";
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async deleteApplication(id) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        await axios.delete(
          import.meta.env.VITE_API_URL + "applications/" + id,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        this.applications = this.applications.filter(
          (application) => application.id != id
        );
        messageStore.success = "Application deleted !";
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async initApplications() {
      const authStore = useAuthStore();
      if (authStore.user.role == "ROLE_ADMIN") {
        await this.fetchApplications();
      } else if (authStore.user.role == "ROLE_CITIZEN") {
        await this.fetchApplicationsByCitizen(authStore.user.username);
      } else if (authStore.user.role == "ROLE_EMPLOYEE") {
        await this.fetchApplicationsByStatus("PENDING");
      } else {
        await this.fetchApplicationsByStatus("VALIDATED");
      }
      console.log(this.applications);
    },
    async fetchApplications() {
      console.log("fetching------------");
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        const res = await axios.get(
          import.meta.env.VITE_API_URL + "applications",
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        this.applications = res.data;
        console.log(res.data);
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async fetchApplicationsByStatus(status) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        const res = await axios.get(
          import.meta.env.VITE_API_URL +
            "applications/ByStatus?status=" +
            status,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        this.applications = res.data;
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async fetchApplicationsByCitizen(citizen_username) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        const res = await axios.get(
          import.meta.env.VITE_API_URL +
            "applications/ByCitizen?citizenUsername=" +
            citizen_username,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        this.applications = res.data;
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async fetchApplication(id) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        const res = await axios.get(
          import.meta.env.VITE_API_URL + "applications/" + id,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        this.currentApplication = res.data;
      } catch (error) {
        console.log(error);
        messageStore.error = error.response.data.message;
      }
    },
    async fetchApplicationFile(application) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        const res = await axios.get(
          import.meta.env.VITE_API_URL +
            "applications/" +
            application.id +
            "/file",
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },

            responseType: "blob",
          }
        );
        const link = document.createElement("a");
        link.href = window.URL.createObjectURL(res.data);
        link.download = application.file || "";
        link.click();
      } catch (error) {
        console.log(error);
        messageStore.error = error.response.data.message;
      }
    },
    async changeStatusInApplication(id, status) {
      const authStore = useAuthStore();
      const messageStore = useMessageStore();
      try {
        const res = await axios.patch(
          import.meta.env.VITE_API_URL + "applications/" + id,
          status,
          {
            headers: {
              Authorization: "Bearer " + authStore.user.token,
            },
          }
        );
        this.updatedApplication = res.data;
        messageStore.success = "Application updated !";
      } catch (error) {
        messageStore.error = error.response.data.message;
      }
    },
    async validateApplication(id) {
      const json = {
        status: "VALIDATED",
      };
      await this.changeStatusInApplication(id, json);
    },
    async approveApplication(id) {
      const json = {
        status: "APPROVED",
      };
      await this.changeStatusInApplication(id, json);
    },
    async rejectApplication(id) {
      const json = {
        status: "REJECTED",
      };
      await this.changeStatusInApplication(id, json);
    },
    canCreateApplication() {
      const authStore = useAuthStore();
      if (
        authStore.user.role == "ROLE_ADMIN" ||
        authStore.user.role == "ROLE_CITIZEN"
      ) {
        return true;
      }
      return false;
    },
    canEditApplication(animal) {
      const authStore = useAuthStore();
      if (authStore.user.role == "ROLE_ADMIN") {
        return true;
      } else if (authStore.user.role == "ROLE_CITIZEN") {
        if (animal.citizen) {
          let citizen = animal.citizen;
          if (citizen.username == authStore.user.username) return true;
        }
      } else {
        return false;
      }
    },
    canDeleteApplication(animal) {
      const authStore = useAuthStore();
      if (authStore.user.role == "ROLE_ADMIN") {
        return true;
      } else if (authStore.user.role == "ROLE_CITIZEN") {
        if (animal.citizen) {
          let citizen = animal.citizen;
          if (citizen.username == authStore.user.username) return true;
        }
      } else {
        return false;
      }
    },
    canValidateApplication(application) {
      const authStore = useAuthStore();
      if (
        (authStore.user.role == "ROLE_ADMIN" ||
          authStore.user.role == "ROLE_EMPLOYEE") &&
        application.status == "PENDING"
      ) {
        return true;
      }
      return false;
    },
    canApproveApplication(application) {
      const authStore = useAuthStore();
      if (
        (authStore.user.role == "ROLE_ADMIN" ||
          authStore.user.role == "ROLE_OFFICER") &&
        application.status == "VALIDATED"
      ) {
        return true;
      }
      return false;
    },
    canRejectApplication(application) {
      const authStore = useAuthStore();
      if (
        (authStore.user.role == "ROLE_ADMIN" ||
          authStore.user.role == "ROLE_OFFICER") &&
        application.status == "VALIDATED"
      ) {
        return true;
      }
      return false;
    },
  },
});
