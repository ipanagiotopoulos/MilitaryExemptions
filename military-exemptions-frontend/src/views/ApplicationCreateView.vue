<template>
  <v-sheet class="bg-white pa-12" rounded>
    <v-card class="mx-auto px-6 py-8">
      <template v-slot:title> Application Create </template>

      <v-container>
        <v-form v-model="form" @submit.prevent="onSubmit">
          <v-select
            label="Select"
            :items="['HEALTH', 'STUDIES']"
            v-model="application.reason"
            :rules="[required]"
            :readonly="loading"
          ></v-select>
          <v-text-field
            v-model="application.milNumber"
            :readonly="loading"
            :rules="NumberRules"
            label="Military Number"
          ></v-text-field>
          <v-file-input
            v-model="file"
            accept=".pdf"
            label="File input"
            :rules="[required]"
            :readonly="loading"
          ></v-file-input>
          <br />
          <v-btn
            :disabled="!form"
            :loading="loading"
            color="success"
            size="large"
            type="submit"
            variant="elevated"
          >
            Submit
          </v-btn>
        </v-form>
      </v-container>
    </v-card>
  </v-sheet>
</template>
<script setup>
import { reactive, ref } from "vue";
import { useApplicationStore } from "../stores/application";
import { useRouter } from "vue-router";

const applicationStore = useApplicationStore();
const router = useRouter();

const application = reactive({
  milNumber: "",
  reason: "",
});

const form = ref(false);
const file = ref(null);
const loading = ref(false);

async function onSubmit() {
  console.log(file.value);
  if (!form.value) return;
  loading.value = true;
  await applicationStore
    .createApplication(application, file.value[0])
    .then(setTimeout(() => (loading.value = false), 2000));
  router.push("/applications");
}

function required(v) {
  return !!v || "Field is required";
}
const NumberRules = [
  (v) => !!v || "Militarity Number is required",
  (v) =>
    (v && /^\d+$/.test(v)) || "Militarity Number must contain only numbers",
];
</script>
