<template>
  <section class="relative w-full h-screen flex items-center justify-start text-white ">
<!--    <div-->
<!--        class="min-h-screen bg-cover bg-center bg-fixed inset-0"-->
<!--        :style="{ backgroundImage: `url(${backgroundImage})` }"-->
<!--    >-->
<!--    <div class="bg-fixed " style="background-image: url('./assets/hero-image.jpg')">-->
      <div class="absolute inset-0 bg-cover bg-center bg-no-repeat">
        <!--    <div class="absolute inset-0 bg-cover bg-center bg-no-repeat" style="background-image: url('/src/assets/hero-image.jpg');">-->
      </div>
          <div class="absolute inset-0 m-0 p-0">
            <img
                src="/src/assets/hero-image.jpg"
                alt="Горы и путешественник"
                class="w-full h-full object-cover brightness-50 border-none object-position-center"
            />
          </div>
      <div class="relative z-10 flex flex-col items-start text-left mx-auto max-w-4xl">
        <!--    <div class="relative z-10 text-left max-w-4xl"> &lt;!&ndash; Increased width by 1.5 times &ndash;&gt;-->
        <h1 class="text-6xl md:text-5xl font-bold leading-tight">
          Авторские экскурсии из Пятигорска, Кисловодска, Железноводска — <br>без суеты и забот!
        </h1>
        <p class="mt-20 text-2xl md:text-2xl">
          Отправьтесь в увлекательное путешествие по живописным горам, старинным крепостям и заповедным местам! <br>
          Заберу вас прямо из отеля, организую маршрут, покажу знаковые и скрытые уголки с историей, которую не найти в
          путеводителях.
        </p>
        <button
            class="mt-20 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-5 px-14 rounded-full text-xl shadow-lg transition">
          <!-- Added full border radius -->
          Выбрать маршрут
        </button>
        <div class="mt-12 grid md:grid-cols-3 gap-8 text-center">
          <div class="flex flex-col items-center">
          <span class="text-xl font-bold relative">
            <i class="pi pi-check text-white absolute left-[-20px] top-[-5px]"></i> <!-- White checkmark -->
            Живые фото
          </span>
            <p class="mt-4 text-lg">кадры, где вы и природа в гармонии (и без лишних туристов в фоне)</p>
          </div>
          <div class="flex flex-col items-center">
          <span class="text-xl font-bold relative">
            <i class="fas fa-check-circle text-white absolute left-[-20px] top-[-5px]"></i> <!-- White checkmark -->
            Уникальные маршруты
          </span>
            <p class="mt-4 text-lg">горные дороги, заброшенные крепости, живописные перевалы</p>
          </div>
          <div class="flex flex-col items-center">
          <span class="text-xl font-bold relative">
            <i class="fas fa-check-circle text-white absolute left-[-20px] top-[-5px]"></i> <!-- White checkmark -->
            Комфорт и забота
          </span>
            <p class="mt-4 text-lg">никакой суеты, только чистый восторг от путешествия</p>
          </div>
        </div>
      </div>
<!--    </div>-->
  </section>
</template>

<script >
//import backgroundImage from './assets/hero-image.jpg';
export default {
  name: "HeroSection",
};
</script>

<style scoped>
/* Ensure the image has no border */
img {
  border: none;
}

h1 {
  line-height: 1.2; /* Задайте нужное значение */
}


</style>