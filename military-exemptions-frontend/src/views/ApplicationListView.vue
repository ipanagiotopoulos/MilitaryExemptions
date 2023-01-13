<template>
  <v-card class="mx-auto" prepend-icon="mdi-application">
    <template v-slot:title>
      Applications
      <v-btn
        v-if="applicationStore.canCreateApplication()"
        @click="createApplication()"
        prepend-icon="mdi-plus"
        color="success"
        size="small"
        class="float-right"
        >Create
      </v-btn></template
    >

    <v-card-text>
      <v-table>
        <thead>
          <tr>
            <th class="text-left">Citizen</th>
            <th class="text-left">Created At</th>
            <th class="text-left">Reason</th>
            <th class="text-left">Military Number</th>
            <th class="text-left">Status</th>
            <th class="text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in applicationStore.applications" :key="item.name">
            <td>{{ item.citizen.full_name }}</td>
            <td>{{ item.createdAt }}</td>
            <td>{{ item.reason }}</td>
            <td>{{ item.milNumber }}</td>
            <td>{{ item.status }}</td>
            <td>
              <v-btn
                variant="outlined"
                v-if="applicationStore.canEditApplication(item)"
                @click="editApplication(item)"
                size="small"
                icon
                color="success"
              >
                <v-icon>mdi-pencil</v-icon>
              </v-btn>
              <v-btn
                variant="outlined"
                v-if="applicationStore.canDeleteApplication(item)"
                @click="deleteApplication(item.id)"
                size="small"
                icon
                color="red"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
              <v-btn
                variant="outlined"
                @click="viewApplication(item.id)"
                size="small"
                icon
                color="info"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { useApplicationStore } from "../stores/application";
import { useRouter, onBeforeRouteUpdate } from "vue-router";

const applicationStore = useApplicationStore();

// eslint-disable-next-line prettier/prettier, no-unused-vars
onBeforeRouteUpdate(async (to, from) => {
  await applicationStore.initApplications();
});
(async () => {
  console.log("initializing");
  await applicationStore.initApplications();
})();

const router = useRouter();

const viewApplication = (id) => {
  router.push({
    name: "application details",
    params: { id: id },
  });
};

const createApplication = () => {
  router.push({
    name: "application create",
  });
};

const editApplication = (row) => {
  router.push({
    name: "application update",
    params: { id: row.id },
  });
};
async function deleteApplication(id) {
  await applicationStore.deleteApplication(id);
}
</script>
