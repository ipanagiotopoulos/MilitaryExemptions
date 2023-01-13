<template>
  <v-sheet class="bg-white pa-12" rounded>
    <v-card class="mx-auto px-6 py-8" max-width="344">
      <v-form v-model="form" @submit.prevent="onSubmit">
        <v-text-field
          v-model="user.username"
          :readonly="loading"
          :rules="[required]"
          class="mb-2"
          clearable
          label="Username"
        ></v-text-field>

        <v-text-field
          v-model="user.password"
          :readonly="loading"
          :rules="[required]"
          clearable
          type="password"
          label="Password"
          placeholder="Enter your password"
        ></v-text-field>
        <br />
        <v-btn
          :disabled="!form"
          :loading="loading"
          block
          color="success"
          size="large"
          type="submit"
          variant="elevated"
        >
          Sign In
        </v-btn>
      </v-form>
    </v-card>
  </v-sheet>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import { useAuthStore } from "../stores/auth";
import { computed } from "vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";

const router = useRouter();

const authStore = useAuthStore();

const isLoggedIn = computed(() => authStore.loggedIn);

const user = reactive({
  username: "",
  password: "",
});

const form = ref(false);

const loading = ref(false);

(async () => {
  if (isLoggedIn.value) {
    router.push("/applications");
  }
})();

const { loggedIn } = storeToRefs(authStore);

async function onSubmit() {
  if (!form.value) return;
  loading.value = true;
  await authStore
    .login(user)
    .then(setTimeout(() => (loading.value = false), 2000));
}

function required(v) {
  return !!v || "Field is required";
}

watch(loggedIn, function () {
  router.push("/applications");
});
</script>
