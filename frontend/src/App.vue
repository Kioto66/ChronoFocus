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
import {
  refAuth,
  login,
  refreshT,
  getAccessToken,
  saveTokens,
  getTokens,
  clearTokens,
  isAuthenticated,
} from '@/services/auth';


const colorsIcons = [
  {label: "Персиковый", value: "bg-red-300", icon: "url('/images/icon-work.svg')"},
  {label: "Голубой", value: "bg-blue-100", icon: "url('/images/icon-play.svg')"},
  {label: "Розовый", value: "bg-red-200", icon: "url('/images/icon-study.svg')"},
  {label: "Зеленый", value: "bg-green-300", icon: "url('/images/icon-exercise.svg')"},
  {label: "Фиолетовый", value: "bg-violet-300", icon: "url('/images/icon-social.svg')"},
  {label: "Желтый", value: "bg-orange-300", icon: "url('/images/icon-self-care.svg')"}
];


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
//const cat = ref([]);
const cat = ref([]);
const errorMessage = ref('');
const refPeriod = ref("month");
const refRunningCategory = ref(0);
const refStartTimestamp = ref(0);
const isLoginVisible=ref(false);

// const handleAuth = async () => {
//   try {
//     const { accessToken, refreshToken, type } = await getTokens();
//     console.log('Токены успешно обновлены!');
//     console.log('Новый Access Token:', accessToken);
//     console.log('Новый Refresh Token:', refreshToken);
//
//     // Очищаем сообщение об ошибке
//     errorMessage.value = '';
//   } catch (error) {
//     // Обрабатываем ошибку
//     errorMessage.value = error.message || 'Произошла ошибка при обновлении токена';
//     alert(errorMessage.value); // Показываем сообщение об ошибке
//   }
// };

const handleAuth = async () => {
  try {
    const  accessToken  = await getAccessToken();
    console.log('Новый Access Token:', accessToken);

    // Очищаем сообщение об ошибке
    errorMessage.value = '';
  } catch (error) {
    // Обрабатываем ошибку
    errorMessage.value = error.message || 'Произошла ошибка при обновлении токена';
    alert(errorMessage.value); // Показываем сообщение об ошибке
  }
};



const selectedCategory = ref({id: 0, name: "", color: "", icon: "", duration: 0});

const toast = useToast();
const newCategory = ref({name: "", color: "", icon: "", duration: 0});
const updateIcon = () => {
  const selectedColor = colorsIcons.find(color => color.value === newCategory.value.color);
  if (selectedColor) {
    newCategory.value.icon = selectedColor.icon;
    console.log(newCategory.value);
  }
};
const isDialogVisible = ref(false);
const isCategoryVisible = ref(false);
const openDialog = () => {
  console.log("dsasdf");
  isDialogVisible.value = true;
};

//****************************************
//старт-стоп
const isRunning = ref(false);// Запущен ли секундомер
const refCurrentTracker = ref(0);
const startTime = ref(0); // Время начала
const elapsedTime = ref(0); // Прошедшее время
const timerInterval = ref(null);

const getCurrentTracker = async () => {
  try {
    const response = await ApiService.getCurrentTracker();
    console.log("получили isRunning:", response);
    if (Array.isArray(response) && response.length > 0) {
      refStartTimestamp.value = response[0]?.startTimestamp;


      // Проверяем, есть ли категория у первого элемента массива
      const categoryId = response[0]?.category?.id;
      if (categoryId) {
        refRunningCategory.value = categoryId;
        console.log("Запущена кат:", response[0]?.category?.name);
      } else {
        console.warn('Категория не найдена у первого элемента');
      }
    } else {
      // Если массив пустой
      console.log("не запущена");
      refRunningCategory.value = 0;
      isRunning.value = false;
      elapsedTime.value = 0;
    }

  } catch (error) {
    console.error("не получили isRunning", error);
  }
}

// const formattedTime = computed(() => {
//   const totalSeconds = Math.floor(elapsedTime.value / 1000);
//   const minutes = Math.floor(totalSeconds / 60);
//   const seconds = totalSeconds % 60;
//   return `${String(minutes).padStart(2, "0")}:${String(seconds).padStart(2, "0")}`;
// });
const formattedTime = computed(() => {
  const totalSeconds = Math.floor(elapsedTime.value / 1000);
  const hours = Math.floor(totalSeconds / 3600); // Часы
  const minutes = Math.floor((totalSeconds % 3600) / 60); // Минуты
  const seconds = totalSeconds % 60; // Секунды
  return `${String(hours).padStart(2, "0")}:${String(minutes).padStart(2, "0")}:${String(seconds).padStart(2, "0")}`;
});

const startStop = async (categoryId) => {
  console.log("СТАРТ_СТОП сравнение категорийИД");
  console.log(categoryId);
  console.log(refRunningCategory.value);
  if (refRunningCategory.value === categoryId) {
    await stopTracker(categoryId);
    // Останавливаем секундомер
    clearInterval(timerInterval.value);
    isRunning.value = false;
  } else {
    if (refRunningCategory.value > 0) {
      await stopTracker(refRunningCategory.value);
      // Останавливаем секундомер
      clearInterval(timerInterval.value);
      isRunning.value = false;
    }

    //Запускаем сервер
    await startTracker(categoryId);
    // Запускаем секундомер
    startTime.value = Date.now() - elapsedTime.value;
    timerInterval.value = setInterval(() => {
      elapsedTime.value = Date.now() - startTime.value;
    }, 10); // Обновляем каждые 10 мс
    isRunning.value = true;
  }
  await getCurrentTracker();
  console.log("после СТАРТ_СТОП категорииИД");
  console.log(categoryId);
  console.log(refRunningCategory.value);
};

const startTracker = async (id) => {
  try {
    console.log("категория старта:", id);
    const response = await ApiService.startTracker(id);
    console.log("стартовали:", response);
  } catch (error) {
    console.error("не стартовали", error);
  }
};

const stopTracker = async (id) => {
  try {
    console.log("категория стопа:", id);
    const response = await ApiService.stopTracker(id);
    console.log("Остановили:", response);
  } catch (error) {
    console.error("не остановили", error);
  }
};


const openCategory = async (category) => {
  selectedCategory.value = JSON.parse(JSON.stringify(category));
  await getCurrentTracker();
  if (refRunningCategory.value !== category.id) {
    console.log("++++++++++++++++++++++");
    console.log(refStartTimestamp.value);
    elapsedTime.value = 0;
    isRunning.value = false;
    timerInterval.value = 0;
  } else {
    //Если категория запущена, то запускаеm ее и здесь
    console.log("***********************");
    console.log(refStartTimestamp.value);

// console.log(refStartTimestamp);
    startTime.value = new Date(refStartTimestamp.value).getTime();
    elapsedTime.value = Date.now() - startTime.value;
    //const startTime = new Date(refStartTimestamp.value).getTime();// Преобразуем метку времени в миллисекунды
    //const currTime =Date.now();

    // startTime.value = Date.now() - elapsedTime.value;
    // timerInterval.value = setInterval(() => {
    //   elapsedTime.value = Date.now() - startTime.value;
    // }, 10); // Обновляем каждые 10 мс
    // isRunning.value = true;

    console.log("////////////////////////");
    timerInterval.value = setInterval(() => {
      elapsedTime.value = Date.now() - startTime.value;
    }, 500);
    isRunning.value = true;
  }
  console.log(category.id);
  console.log(refRunningCategory.value);
  console.log('значения секундомера');
  console.log("timerInterval", timerInterval.value);
  console.log("elapsedTime", elapsedTime.value);
  console.log("startTime", startTime.value);
  console.log("isRunning", isRunning.value);
  isCategoryVisible.value = true;
};
//********************************
//Расчет дат
const getDateRange = (period = "month") => {
  const now = new Date(); // Текущая дата и время
  let startDate;

  switch (period) {
    case "day":
      // Начало текущего дня
      startDate = new Date(now.getFullYear(), now.getMonth(), now.getDate());
      refPeriod.value = "day";

      break;

    case "week":
      // Начало текущей недели (понедельник)
      const dayOfWeek = now.getDay(); // 0 (воскресенье) - 6 (суббота)
      const diff = now.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1); // Коррекция для воскресенья
      startDate = new Date(now.getFullYear(), now.getMonth(), diff);
      refPeriod.value = "week";
      break;

    case "month":
      // Начало текущего месяца
      startDate = new Date(now.getFullYear(), now.getMonth(), 1);
      refPeriod.value = "month";
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
//преобразование объекта duration в строку
const formatDuration = (duration) => {
  const pad = (num) => String(num).padStart(2, '0'); // Добавляет ведущий 0, если нужно
  //return `${pad(duration.hours)}:${pad(duration.minutes)}:${pad(duration.seconds)}`;
  return `${pad(duration.hours)}:${pad(duration.minutes)}`;
};
//*****************************
//Получаем отчет
const getSummaryResults = async (period) => {
  try {
    // Получаем даты начала и конца периода
    const {from, to} = getDateRange(period);

    const summary = await ApiService.getSummary(from, to);
    console.log("Сводка за период:", summary);
    console.log("период:", refPeriod.value);
    //*******************
    if (!summary || summary.length === 0) return;

    // Получаем массив body из входящих данных
    const incomingCategories = summary[0].body;

    // Обновляем duration в массиве cat
    incomingCategories.forEach((incomingCat) => {
      const existingCat = cat.value.find((c) => c.id === incomingCat.id);

      if (existingCat) {
        // Если категория найдена, обновляем duration
        existingCat.duration = formatDuration(incomingCat.duration);
      } else {
        // Если категории ещё нет, добавляем новую
        cat.value.push({
          id: incomingCat.id,
          name: incomingCat.name,
          color: incomingCat.color,
          icon: incomingCat.icon,
          isRunning: false,
          duration: incomingCat.duration
        });
      }
      cat.value.forEach((existingCat) => {
        const found = incomingCategories.find((incomingCat) => incomingCat.id === existingCat.id);
        if (!found) {
          existingCat.duration = '00:00';
        }
      });
      console.log(existingCat);
    });
    console.log("ДОбавляем duration");
    console.log(cat.value);

    //********************************
  } catch (error) {
    console.error("Не удалось получить сводку:", error);
  }
};
//********************************
// Получить все категории
const getCategories = async () => {
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

const refresh = async() =>{
  await getCategories();
  await getSummaryResults();
  await getCurrentTracker();
}
//Отслеживаю закрытие окна с категорией, сбрасываю таймер и текущую кат
watch(isCategoryVisible, (newVal) => {
  if (!newVal) {
    selectedCategory.value = {id: 0, name: "", color: "", icon: "", duration: 0};
    clearInterval(timerInterval.value);
    elapsedTime.value = 0;
    isRunning.value = false;// Очищаем данные при закрытии окна
  }
});

onMounted(refresh);
// onMounted(getSummaryResults);
// onMounted(getCurrentTracker);


</script>

<template>
  <main class="grid grid-cols-1 lg:grid-cols-4 p-4 bg-blue-900 text-white ">
    <section class=" lg:row-span-2 bg-blue-600 rounded-xl m-4">
      <div class="grid grid-cols-[80px_1fr] lg:grid-cols-1 lg:h-96 bg-primary p-8 rounded-xl">
        <img src="/images/image-kioto-2.jpg"
             class="rounded-full border-2 border-white w-20 h-20 lg:w-40 lg:h-40
             hover:bg-gray-800 cursor-pointer" @click="isLoginVisible=true">
        <div>

          <p class="text-lg text-blue-200">Report for</p>
          <p class="text-3xl text-blue-200 lg:text-5xl lg:text-white">Ярослав</p>
        </div>
      </div>
      <div class="grid grid-cols-3 lg:grid-cols-1 lg:gap-4  p-4">
        <span class="text-center lg:text-left cursor-pointer hover:text-xl"
              :class="refPeriod === 'day' ? 'text-white' : 'text-blue-200'"
              @click="getSummaryResults('day')"> Daily </span>
        <span class="text-center lg:text-left cursor-pointer hover:text-xl"
              :class="refPeriod === 'week' ? 'text-white' : 'text-blue-200'"
              @click="getSummaryResults('week')">  Weekly </span>
        <span class="text-center lg:text-left text-blue-200 cursor-pointer hover:text-xl"
              :class="refPeriod === 'month' ? 'text-white' : 'text-blue-200'"
              @click="getSummaryResults('month')">  Monthly </span>
      </div>
    </section>

    <section
        v-for="(category, index) in cat" :key="category.id"
        :class="`card-top ${category.color}`"
        :style="{ backgroundImage: category.icon }"
    >
      <div class="h-12 ">
      </div>
      <div class="card-cat  " :class="refRunningCategory===category.id ? 'text-red-200':''"
           @click="openCategory(category)">
        <div class="grid grid-cols-2 ">
          <div class="text-xl font-bold"> {{ category.name }}</div>
          <div class="justify-end text-2xl  text-end">...</div>
          <button label="button" class="text-white"></button>
        </div>

        <div class="cat-result">
          <div class="text-5xl text-center"> {{ category.duration }}</div>  <!-- daily -->
          <div class="last-result">Previous - 29hrs</div>
        </div>
      </div>
    </section>
    <section class="card-top">
      <div class=" min-h-36 h-full w-full p-6 rounded-xl bg-[url('/images/icon-work1.svg')]
      bg-no-repeat bg-center cursor-pointer hover:bg-blue-600" @click="openDialog">
        <!--        <p class="text-center text-white/10 text-2xl"> Добавить </p>-->


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
      <div class="p-2  rounded-xl" :class="newCategory.color || 'bg-blue-100'">
        <div class="card flex flex-col  gap-4">
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
          <Button label="Добавить" @click="createCategory" class="mt-4"/>
        </div>
      </div>
    </Dialog>

    <Dialog
        v-model:visible="isLoginVisible"
        header="Авторизация"
        modal
        :contentStyle="{ backgroundColor: '##1a1d42' }"
        :headerStyle="{ backgroundColor: '##1a1d42' }"
        class="dark"
    >
      <div class="p-2  rounded-xl" :class="newCategory.color || 'bg-blue-300'">
        <div class="card flex flex-col  gap-4">
          <label class="block ">введите имя и пароль</label>
          <InputText
              v-model="refAuth.login"
              class="w-full mt-2 "
              placeholder="Введите имя"
          />
          <label class="block ">Пароль</label>
          <InputText
              v-model="refAuth.password"
              class="w-full mt-2 "
              placeholder="Введите пароль"
          />
          <Button label="Войти" @click="handleAuth" class="mt-4"/>
        </div>
      </div>
    </Dialog>

    <Dialog
        v-model:visible="isCategoryVisible"
        header="Что-то написано"
        modal
        :showHeader="true"
        class="no-border-dialog"
    >
      <template #header>
        <div >
          <h2 class="text-white text-5xl text-center">{{ selectedCategory.name }}</h2>
        </div>

      </template>
      <div
          class="pt-20 rounded-3xl"
          :class="selectedCategory.color || 'bg-blue-100'"
          :style="selectedCategory.icon.length > 0 ? {
    backgroundImage: selectedCategory.icon,
    backgroundPosition: '95% 2%',
    backgroundRepeat: 'no-repeat',
  } : {}">
        <div class="card rounded-3xl flex flex-col  gap-4">
<!--          <label class="block text-5xl text-center">{{ selectedCategory.name }}</label>-->
<!--          <label class="block text-2xl text-center">{{ selectedCategory.icon }}</label>-->
          <div class="min-h-64 min-w-64">
            <div class="text-[8rem] " :class="isRunning ? 'text-white' : 'text-gray-500'">
              {{ formattedTime }}
            </div>
          </div>

          <Button label="Старт/Стоп" @click="startStop(selectedCategory.id)" class="mt-4"/>
        </div>
      </div>
    </Dialog>

<!--    <Dialog-->
<!--        v-model:visible="isCategoryVisible"-->
<!--        header="Что-то написано"-->
<!--        modal-->
<!--        :showHeader="true"-->
<!--        class="no-border-dialog"-->
<!--    >-->
<!--      <template #header>-->
<!--        <div class="bg-gradient-to-r from-blue-500 to-purple-600 p-4">-->
<!--          <h2 class="text-white text-xl font-bold">Что-то написано</h2>-->
<!--        </div>-->
<!--      </template>-->
<!--      <div-->
<!--          class="pt-20 rounded-2xl"-->
<!--          :class="selectedCategory.color || 'bg-blue-100'"-->
<!--          :style="selectedCategory.icon.length > 0 ? {-->
<!--      backgroundImage: selectedCategory.icon,-->
<!--      backgroundPosition: '85% 2%',-->
<!--      backgroundRepeat: 'no-repeat',-->
<!--    } : {}"-->
<!--      >-->
<!--        <div class="card flex flex-col gap-4">-->
<!--          <label class="block text-5xl text-center">{{ selectedCategory.name }}</label>-->
<!--          <label class="block text-2xl text-center">{{ selectedCategory.icon }}</label>-->
<!--          <div class="min-h-64 min-w-64">-->
<!--            <div class="text-[8rem]" :class="isRunning ? 'text-white' : 'text-gray-500'">-->
<!--              {{ formattedTime }}-->
<!--            </div>-->
<!--          </div>-->
<!--          <Button label="Старт/Стоп" @click="startStop(selectedCategory.id)" class="mt-4"/>-->
<!--        </div>-->
<!--      </div>-->
<!--    </Dialog>-->
  </main>

</template>

<style scoped>
.no-border-dialog .p-dialog {
  border-radius: 12px !important;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2), 0 0 40px rgba(0, 0, 255, 0.3) !important;
  animation: fadeIn 0.3s ease-in-out;
}

.no-border-dialog .p-dialog-content {
  border: none !important;
  background: rgba(255, 255, 255, 0.9) !important;
}

.no-border-dialog .p-dialog-header {
  border: none !important;
  background: linear-gradient(90deg, #4f46e5, #9333ea) !important;
  color: white !important;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>