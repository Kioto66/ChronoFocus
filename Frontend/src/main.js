//import './assets/main.css'
import PrimeVue from 'primevue/config';
import Aura from '@primevue/themes/aura';
import './assets/tailwind.css';
import "primeicons/primeicons.css";
//import router from "./router";
import Toast from "primevue/toast";
import ToastService from "primevue/toastservice";

import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App);
app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
    // component: {
    //     Toast : Toast
    // }
})
    .use(ToastService)
    //.use(router);
app.mount("#app");
//*****************************************



