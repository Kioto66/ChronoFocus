<script setup>
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import Toast from "primevue/toast";
import Checkbox from "primevue/checkbox";
import Card from 'primevue/card';
import Select from "primevue/select";
import {computed, onMounted, ref, watch} from "vue";
import Password from "primevue/password";
import axios from "axios";
import {useToast} from 'primevue/usetoast';
import ProgressSpinner from "primevue/progressspinner";
import Dialog from "primevue/dialog";
import ApiService from "@/services/api";


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

// const cat = ref([
//   {
//     id: 0,
//     name: "Work",
//     color: "bg-red-300",
//     icon: "url('/public/images/icon-work.svg')",
//     isRunning: false,
//     duration: 8
//   },
//   {
//     id: 0,
//     name: "Play",
//     color: "bg-blue-100",
//     icon: "url('/public/images/icon-play.svg')",
//     isRunning: false,
//     duration: 10
//   },
//   {
//     id: 0,
//     name: "Study",
//     color: "bg-red-200",
//     icon: "url('/public/images/icon-study.svg')",
//     isRunning: false,
//     duration: 0
//   },
//   {
//     id: 0,
//     name: "VueJS",
//     color: "bg-green-300",
//     icon: "url('/public/images/icon-exercise.svg')",
//     isRunning: false,
//     duration: 4
//   },
//   {
//     id: 0,
//     name: "PostgreSQL",
//     color: "bg-violet-300",
//     icon: "url('/public/images/icon-social.svg')",
//     isRunning: false,
//     duration: 0
//   }
//   // {id:0,  name:"Спорт", color: "bg-orange-300",  icon: "url('/public/images/icon-self-care.svg')",  isRunning: false,  duration:0}
// ]);
const cat = ref([]);
const toast = useToast();
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

const openDialog2 = (category)=> {
  console.log(category.name);
  //isDialogVisible.value = true;
};
//********************************
//Расчет дат
const getDateRange = (period) => {
  const now = new Date(); // Текущая дата и время
  let startDate;

  switch (period) {
    case "day":
      // Начало текущего дня
      startDate = new Date(now.getFullYear(), now.getMonth(), now.getDate());
      break;

    case "week":
      // Начало текущей недели (понедельник)
      const dayOfWeek = now.getDay(); // 0 (воскресенье) - 6 (суббота)
      const diff = now.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1); // Коррекция для воскресенья
      startDate = new Date(now.getFullYear(), now.getMonth(), diff);
      break;

    case "month":
      // Начало текущего месяца
      startDate = new Date(now.getFullYear(), now.getMonth(), 1);
      break;

    default:
      throw new Error("Неизвестный период");
  }

  // Форматируем даты в формат YYYY-MM-DD с использованием локального времени
  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // Месяцы начинаются с 0
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  return {
    from: formatDate(startDate), // Начало периода
    to: formatDate(now), // Текущее время
  };
};

//*****************************
//Получаем отчет
const fetchSummary = async (period) => {
  try {
    // Получаем даты начала и конца периода
    const { from, to } = getDateRange(period);

    // Отправляем запрос на сервер
    console.log("begin", from);
    console.log("end", to);
    const summary = await ApiService.getSummary(from, to);
    console.log("Сводка за период:", summary);
  } catch (error) {
    console.error("Не удалось получить сводку:", error);
  }
};
//********************************
// Получить все категории
const fetchCategories = async () => {
  try {
    const categories = await ApiService.getCategories();
    cat.value = Array.isArray(categories) ? categories.flat() : [];
    // cat.value = categories.flat();
    console.log("Kat", cat.value);

  } catch (error) {
    console.error("Не удалось получить категории:", error);
  }
};

// Создать новую категорию
const createCategory = async () => {
  if (newCategory.value.name.trim()) try {
    console.log("Создание", newCategory.value)
    const response = await ApiService.createCategory(newCategory.value);
    console.log("Категория создана:", response);
  } catch (error) {
    console.error("Не удалось создать категорию:", error);
  }
};
onMounted(fetchCategories);
// const addCategory = async () => {
//   if (newCategory.value.name.trim()) {
//     try {
//       const requestBody = {
//         apiVer: "1",
//         payload: [
//           {
//               color: newCategory.value.color,
//               name: newCategory.value.name,
//             icon: newCategory.value.icon
//             },
//         ],
//         status: "OK",
//       };
//
//       const response = await axios.post("http://127.0.0.1:8080/category", requestBody);
//
//       if (response.status === 200) {
//         cat.value.push({ ...newCategory.value, id: cat.value.length + 1 });
//         newCategory.value = { name: "", color: "bg-gray-300", icon: "", duration: 0 };
//         isDialogVisible.value = false;
//         toast.add({ severity: "success", summary: "Успех", detail: "Категория добавлена", life: 3000 });
//       }
//     } catch (error) {
//       console.error("Ошибка при добавлении категории:", error);
//       toast.add({ severity: "error", summary: "Ошибка", detail: "Не удалось добавить категорию", life: 3000 });
//     }
//   }
// };

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
        <span class="text-center lg:text-left text-blue-200 cursor-pointer hover:text-xl" @click="fetchSummary('day')"> Daily </span>
        <span class="text-center lg:text-left cursor-pointer hover:text-xl" @click="fetchSummary('week')">  Weekly </span>
        <span class="text-center lg:text-left text-blue-200 cursor-pointer hover:text-xl" @click="fetchSummary('month')">  Monthly </span>
      </div>
    </section>

    <section
        v-for="(category, index) in cat" :key="category.id"
        :class="`card-top ${category.color}`"
        :style="{ backgroundImage: category.icon }"
    >
      <div class="h-12 ">
      </div>
      <div class="card-cat" @click="openDialog2(category)">
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
        <svg width="100" height="100" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg" fill="none" stroke-linecap="round" stroke-linejoin="round">
          <!-- Основная часть портфеля -->
          <rect x="20" y="30" width="60" height="40" rx="5" ry="5" fill="#4A5D73" stroke="#2C3E50" stroke-width="3"/>

          <!-- Ручка портфеля -->
          <rect x="35" y="20" width="30" height="10" rx="3" ry="3" fill="#4A5D73" stroke="#2C3E50" stroke-width="3"/>

          <!-- Застежка -->
          <rect x="45" y="48" width="10" height="12" fill="#2C3E50" stroke="#2C3E50" stroke-width="2"/>
        </svg>
      </div>
    </section>

    <Dialog
        v-model:visible="isDialogVisible"
        header="Добавить категорию"
        modal
        :contentStyle="{ backgroundColor: '##1a1d42' }"
        :headerStyle="{ backgroundColor: '##1a1d42' }"
        class="dark"
    >
      <div class="p-4 dark">
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
        <Button label="Добавить" @click="createCategory" class="mt-4" />
      </div>
    </Dialog>


  </main>

</template>

<style scoped>

</style>