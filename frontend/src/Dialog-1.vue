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


const colorsIcons = [
  { label: "Персиковый", value: "bg-red-300", icon: "url('/images/icon-work.svg')"},
  { label: "Голубой", value: "bg-blue-100", icon: "url('/images/icon-play.svg')" },
  { label: "Розовый", value: "bg-red-200", icon: "url('/images/icon-study.svg')" },
  { label: "Зеленый", value: "bg-green-300", icon: "url('/images/icon-exercise.svg')" },
  { label: "Фиолетовый", value: "bg-violet-300", icon: "url('/images/icon-social.svg')" },
  { label: "Желтый", value: "bg-orange-300", icon: "url('/images/icon-self-care.svg')" }
];

// const icons = [
//   { label: "Работа", value: "url('/images/icon-work.svg')" },
//   { label: "Игра", value: "url('/images/icon-play.svg')" },
//   { label: "Учеба", value: "url('/images/icon-study.svg')" },
//   { label: "Спорт", value: "url('/images/icon-exercise.svg')" },
//   { label: "Соцсети", value: "url('/images/icon-social.svg')" },
//   { label: "Самопомощь", value: "url('/images/icon-self-care.svg')" }
// ];

const cat = ref([
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
  }
  // {id:0,  name:"Спорт", color: "bg-orange-300",  icon: "url('/public/images/icon-self-care.svg')",  isRunning: false,  duration:0}
]);
const newCategory = ref({ name: "", color: "", icon: "", duration: 0 });
const updateIcon = () => {
  const selectedColor = colorsIcons.find(color => color.value === newCategory.value.color);
  if (selectedColor) {
    newCategory.value.icon = selectedColor.icon;
    console.log(newCategory.value);
  }
};
const isDialogVisible = ref(false);
const openDialog = ()=> {
  console.log("dsasdf");
  isDialogVisible.value = true;
};
const addCategory = () => {
  if (newCategory.value.name.trim()) {
    console.log(newCategory.value);
    cat.value.push({ ...newCategory.value, id: cat.value.length + 1 });
    newCategory.value = { name: "", color: "bg-gray-300", icon: "", duration: 0 };
    isDialogVisible.value = false;
  }
};

</script>

<template>
  <main class="grid grid-cols-1 lg:grid-cols-4 p-4 bg-blue-900 text-white ">
    <section class=" lg:row-span-2 bg-blue-600 rounded-xl m-4">
      <div class="grid grid-cols-[80px_1fr] lg:grid-cols-1 lg:h-96 bg-primary p-8 rounded-xl">
        <img src="/images/image-kioto-2.jpg"
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
    <section class="card-top">
      <div class=" h-full w-full p-6 rounded-xl bg-[url('/images/icon-work1.svg')]
      bg-no-repeat bg-center cursor-pointer hover:bg-blue-600" @click="openDialog">
        <p class="text-center text-white/10 text-2xl"> Добавить </p>
      </div>
    </section>

    <Dialog
        v-model:visible="isDialogVisible"
        header="Добавить категорию"
        modal
        :contentStyle="{ backgroundColor: '##1a1d42' }"
        :headerStyle="{ backgroundColor: '##1a1d42' }"
    >
      <div class="p-4">
        <label class="block ">Что будем считать?</label>
        <InputText
            v-model="newCategory.name"
            class="w-full mt-2 "
            placeholder="Введите категорию"
        />
        <label class="block ">Цвет</label>
        <Select
            v-model="newCategory.color"
            :options="colorsIcons"
            optionLabel="label"
            optionValue="value"
            optionIcon="Icon"
            placeholder="Выберите цвет"
            @change="updateIcon"
            class="w-full mt-2"
        />
        <Button label="Добавить" @click="addCategory" class="mt-4" />
      </div>
    </Dialog>


  </main>

</template>

<style scoped>

</style>