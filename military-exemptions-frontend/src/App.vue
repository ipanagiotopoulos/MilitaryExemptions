<script setup>
import { storeToRefs } from "pinia";
import { useAuthStore } from "./stores/auth";
import { useMessageStore } from "./stores/message";
import { computed, watch, ref } from "vue";
import { useRouter } from "vue-router";

const authStore = useAuthStore();

const messageStore = useMessageStore();

const loggedIn = computed(() => authStore.loggedIn);

const { success, error } = storeToRefs(messageStore);

const success_snackbar = ref(false);
const alert_snackbar = ref(false);
const text = ref("");

const router = useRouter();

const logout = () => {
  authStore.logout();
  router.push({ name: "login" });
};
watch(success, function () {
  if (success.value != "") {
    success_snackbar.value = true;
    text.value = success.value;
  }
  success.value = "";
});

watch(error, function () {
  if (error.value != "") {
    alert_snackbar.value = true;
    text.value = error.value;
  }
  error.value = "";
});
</script>

<template>
  <v-app>
    <v-app-bar color="success" prominent>
      <v-toolbar-title>Military Exemptions</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn v-if="loggedIn" prepend-icon="mdi-application" to="/applications"
        >Applications</v-btn
      >

      <v-btn v-if="loggedIn" @click="logout()" prepend-icon="mdi-logout"
        >Logout
      </v-btn>
    </v-app-bar>

    <v-main>
      <v-container>
        <RouterView />
      </v-container>
    </v-main>
    <v-snackbar
      v-model="success_snackbar"
      :timeout="2000"
      color="success"
      variant="outlined"
    >
      {{ text }}
    </v-snackbar>
    <v-snackbar
      v-model="alert_snackbar"
      :timeout="2000"
      color="red"
      variant="outlined"
    >
      {{ text }}
    </v-snackbar>
  </v-app>
</template>

<style scoped></style>