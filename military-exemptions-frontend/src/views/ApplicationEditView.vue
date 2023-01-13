<template>
  <v-sheet class="bg-white pa-12" rounded>
    <v-card class="mx-auto px-6 py-8">
      <template v-slot:title> Application Update </template>
      <v-container>
        <v-form v-model="form" @submit.prevent="onSubmit">
          <v-select
            label="Select"
            :items="['HEALTH', 'STUDIES']"
            v-model="currentApplication.reason"
            :rules="[required]"
          ></v-select>
          <v-text-field
            v-model="currentApplication.milNumber"
            :rules="NumberRules"
            label="Military Number"
          ></v-text-field>
          <v-file-input
            v-model="file"
            accept=".pdf"
            label="File input"
          ></v-file-input>
          <br />
          <v-btn color="success" size="large" type="submit" variant="elevated">
            Submit
          </v-btn>
        </v-form>
      </v-container>
    </v-card>
  </v-sheet>
</template>
<script setup>
import { ref } from "vue";
import { useApplicationStore } from "../stores/application";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";

const props = defineProps(["id"]);

const applicationStore = useApplicationStore();

const file = ref(null);

const router = useRouter();

const { currentApplication } = storeToRefs(applicationStore);

(async () => {
  applicationStore.fetchApplication(props.id);
  console.log(currentApplication.value);
})();

const form = ref(false);

async function onSubmit() {
  let file_to_send = null;
  if (file.value != null) {
    file_to_send = file.value[0];
  }
  if (!form.value) return;
  await applicationStore.updateApplication(
    props.id,
    currentApplication.value,
    file_to_send
  );
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
  