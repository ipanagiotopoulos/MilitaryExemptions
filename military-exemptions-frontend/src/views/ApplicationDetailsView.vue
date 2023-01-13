<template>
  <v-sheet class="bg-white pa-12" rounded>
    <v-card class="mx-auto px-6 py-8" prepend-icon="mdi-application">
      <template v-slot:title> Application Details </template>
      <v-container>
        <v-row v-if="currentApplication.citizen" justify="center">
          <v-col class="font-weight-bold" cols="4">
            <v-sheet class="pa-2 ma-2"> Citizen : </v-sheet>
          </v-col>
          <v-col cols="4">
            <v-sheet class="pa-2 ma-2">
              {{ currentApplication.citizen.full_name }}
            </v-sheet>
          </v-col>
        </v-row>
        <v-divider inset></v-divider>
        <v-row justify="center">
          <v-col class="font-weight-bold" cols="4">
            <v-sheet class="pa-2 ma-2"> Created At : </v-sheet>
          </v-col>
          <v-col cols="4">
            <v-sheet class="pa-2 ma-2">
              {{ currentApplication.createdAt }}
            </v-sheet>
          </v-col>
        </v-row>
        <v-divider inset></v-divider>
        <v-row justify="center">
          <v-col class="font-weight-bold" cols="4">
            <v-sheet class="pa-2 ma-2"> Reason : </v-sheet>
          </v-col>
          <v-col cols="4">
            <v-sheet class="pa-2 ma-2">
              {{ currentApplication.reason }}
            </v-sheet>
          </v-col>
        </v-row>
        <v-divider inset></v-divider>
        <v-row justify="center">
          <v-col class="font-weight-bold" cols="4">
            <v-sheet class="pa-2 ma-2"> Military Number : </v-sheet>
          </v-col>
          <v-col cols="4">
            <v-sheet class="pa-2 ma-2">
              {{ currentApplication.milNumber }}
            </v-sheet>
          </v-col>
        </v-row>
        <v-divider inset></v-divider>
        <v-row justify="center">
          <v-col class="font-weight-bold" cols="4">
            <v-sheet class="pa-2 ma-2"> Status : </v-sheet>
          </v-col>
          <v-col cols="4">
            <v-sheet class="pa-2 ma-2">
              {{ currentApplication.status }}
            </v-sheet>
          </v-col>
        </v-row>
        <v-divider inset></v-divider>
      </v-container>
      <v-card-actions>
        <v-btn
          v-if="applicationStore.canEditApplication(currentApplication)"
          @click="editApplication(currentApplication.id)"
          color="success"
          variant="outlined"
        >
          Update
        </v-btn>
        <v-btn
          v-if="applicationStore.canDeleteApplication(currentApplication)"
          @click="deleteApplication(currentApplication.id)"
          color="red"
          variant="outlined"
        >
          Delete
        </v-btn>
        <v-btn
          v-if="applicationStore.canValidateApplication(currentApplication)"
          @click="validateApplication(currentApplication.id)"
          color="info"
          variant="outlined"
        >
          Validate
        </v-btn>
        <v-btn
          v-if="applicationStore.canApproveApplication(currentApplication)"
          @click="approveApplication(currentApplication.id)"
          color="teal"
          variant="outlined"
        >
          Approve
        </v-btn>
        <v-btn
          v-if="applicationStore.canRejectApplication(currentApplication)"
          @click="rejectApplication(currentApplication.id)"
          color="indigo"
          variant="outlined"
        >
          Reject
        </v-btn>
        <v-btn @click="getFile()" color="info" variant="outlined">
          Download File
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-sheet>
</template>
<script setup>
import { useApplicationStore } from "../stores/application";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";

const props = defineProps(["id"]);

const applicationStore = useApplicationStore();

const router = useRouter();

const { currentApplication } = storeToRefs(applicationStore);

(async () => {
  applicationStore.fetchApplication(props.id);
})();

async function deleteApplication(id) {
  await applicationStore.deleteApplication(id);
  router.push("/applications");
}
async function validateApplication(id) {
  await applicationStore.validateApplication(id);
  router.push("/applications");
}
async function approveApplication(id) {
  await applicationStore.approveApplication(id);
  router.push("/applications");
}
async function rejectApplication(id) {
  await applicationStore.rejectApplication(id);
  router.push("/applications");
}
async function editApplication(id) {
  router.push("/applications/" + id + "/update");
}
async function getFile() {
  await applicationStore.fetchApplicationFile(currentApplication.value);
}
</script>
    