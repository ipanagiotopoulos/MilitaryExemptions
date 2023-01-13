import { createRouter, createWebHistory } from "vue-router";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
import ApplicationListView from "../views/ApplicationListView.vue";
import ApplicationEditView from "../views/ApplicationEditView.vue";
import ApplicationCreateView from "../views/ApplicationCreateView.vue";
import ApplicationDetailsView from "../views/ApplicationDetailsView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "login",
      component: LoginView,
    },
    {
      path: "/register",
      name: "register",
      component: RegisterView,
    },
    {
      path: "/applications",
      name: "applications",
      component: ApplicationListView,
      meta: {
        isAuthenticated: true,
      },
    },
    {
      path: "/applications/create",
      name: "application create",
      component: ApplicationCreateView,
      meta: {
        isAuthenticated: true,
        isCitizen: true,
        isOfficer: false,
        isEmployee: false,
      },
    },
    {
      path: "/applications/:id/update",
      name: "application update",
      component: ApplicationEditView,
      meta: {
        isAuthenticated: true,
        isCitizen: true,
        isVet: false,
        isEmployee: false,
      },
      props: true,
    },
    {
      path: "/applications/:id/details",
      name: "application details",
      component: ApplicationDetailsView,
      meta: {
        isAuthenticated: true,
      },
      props: true,
    },
    {
      path: "/:catchAll(.*)*",
      name: "Error",
      component: () => import("../views/ErrorView.vue"),
    },
  ],
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.isAuthenticated)) {
    if (localStorage.getItem("user") == null) {
      next({
        path: "/",
        params: { nextUrl: to.fullPath },
      });
    } else {
      let user = JSON.parse(localStorage.getItem("user"));
      console.log(user);
      if (to.matched.some((record) => record.meta.isCitizen)) {
        if (user.role == "ROLE_CITIZEN" || user.role == "ROLE_ADMIN") {
          next();
        } else {
          next({ name: "Error" });
        }
      } else if (to.matched.some((record) => record.meta.isEmployee)) {
        if (user.role == "ROLE_EMPLOYEE" || user.role == "ROLE_ADMIN") {
          next();
        } else {
          next({ name: "Error" });
        }
      } else if (to.matched.some((record) => record.meta.isOfficer)) {
        if (user.role == "ROLE_OFFICER" || user.role == "ROLE_ADMIN") {
          next();
        } else {
          next({ name: "Error" });
        }
      } else {
        next();
      }
    }
  } else {
    next();
  }
});
export default router;
