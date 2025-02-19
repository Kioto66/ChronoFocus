<script setup>
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import Toast from "primevue/toast";
import Checkbox from "primevue/checkbox";
import Card from 'primevue/card';
import Select from "primevue/select";
import {computed, ref, watch} from "vue";
import Password from "primevue/password";
import axios from "axios";
import {useToast} from 'primevue/usetoast';
import ProgressSpinner from "primevue/progressspinner";
import Dialog from "primevue/dialog";

const cat = [
  {
    id: 0,
    name: "Work",
    color: "bg-red-300",
    icon: "url('/public/images/icon-work.svg')",
    isRunning: false,
    duration: 8
  },
  {
    id: 0,
    name: "Play",
    color: "bg-blue-100",
    icon: "url('/public/images/icon-play.svg')",
    isRunning: false,
    duration: 10
  },
  {
    id: 0,
    name: "Study",
    color: "bg-red-200",
    icon: "url('/public/images/icon-study.svg')",
    isRunning: false,
    duration: 0
  },
  {
    id: 0,
    name: "VueJS",
    color: "bg-green-300",
    icon: "url('/public/images/icon-exercise.svg')",
    isRunning: false,
    duration: 4
  },
  {
    id: 0,
    name: "PostgreSQL",
    color: "bg-violet-300",
    icon: "url('/public/images/icon-social.svg')",
    isRunning: false,
    duration: 0
  },
  // {id:0,  name:"Спорт", color: "bg-orange-300",  icon: "url('/public/images/icon-self-care.svg')",  isRunning: false,  duration:0}
];
//const newCategory = ref({ name: "", color: "bg-gray-300", icon: "", duration: 0 });

const isDialogVisible = ref(false);
function openDialog  () {
  console.log("dsasdf");
  isDialogVisible.value = true;
};


</script>

<template>
  <main class="grid grid-cols-1 lg:grid-cols-4 p-4 bg-blue-900 text-white">
    <section class=" lg:row-span-2 bg-blue-600 rounded-xl m-4">
      <div class="grid grid-cols-[80px_1fr] lg:grid-cols-1 lg:h-96 bg-primary p-8 rounded-xl">
        <img src="/public/images/image-kioto-2.jpg"
             class="rounded-full border-4 border-white w-20 h-20 lg:w-40 lg:h-40">
        <div>

          <p class="text-lg text-blue-200">Report for</p>
          <p class="text-3xl text-blue-200 lg:text-5xl lg:text-white">Ярослав</p>
        </div>
      </div>
      <div class="grid grid-cols-3 lg:grid-cols-1 lg:gap-4  p-4">
        <span class="text-center lg:text-left text-blue-200 cursor-pointer hover:text-xl"> Daily </span>
        <span class="text-center lg:text-left cursor-pointer hover:text-xl">  Weekly </span>
        <span class="text-center lg:text-left text-blue-200 cursor-pointer hover:text-xl">  Monthly </span>
      </div>
    </section>

    <section
        v-for="(category, index) in cat" :key="category.id"
        :class="`card-top ${category.color}`"
        :style="{ backgroundImage: category.icon }"
    >
      <div class="h-12 ">
      </div>
      <div class="card-cat">
        <div class="grid grid-cols-2">
          <div class="text-xl font-bold"> {{ category.name }}</div>
          <div class="justify-end text-2xl  text-end">...</div>
          <button label="button" class="text-white"></button>
        </div>

        <div class="cat-result">
          <div class="text-5xl font-mono"> {{ category.duration }}hrs</div>  <!-- daily -->
          <div class="last-result">Previous - 29hrs</div>
        </div>
      </div>
    </section>
    <section>
      <div class=" h-64 w-64 p-6 ml-4 mt-4 rounded-xl bg-[url('/public/images/icon-work1.svg')]
      bg-no-repeat bg-center cursor-pointer hover:bg-blue-600" @click="openDialog">
        <p class="text-center text-white/10 text-2xl"> Добавить dfsgsd</p>
      </div>
    </section>

    <Dialog v-model:visible="isDialogVisible" header="Добавить категорию" modal class="h-80 w-80">
      <p> что-то там напbсyао</p>

    </Dialog>


  </main>

</template>

<style scoped>

</style>