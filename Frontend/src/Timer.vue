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


</script>

<template>
  <main class="grid grid-cols-1 lg:grid-cols-4 p-4 bg-blue-900 text-white">
    <section class=" lg:row-span-2 bg-blue-600 rounded-xl m-4">
      <div class="grid grid-cols-[80px_1fr] lg:grid-cols-1 lg:h-96 bg-primary p-8 rounded-xl">
        <img src="/public/images/image-kioto-2.jpg" class="rounded-full border-4 border-white w-20 h-20 lg:w-40 lg:h-40">
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


    <section class="bg-red-300 rounded-2xl m-4 border-2 border-blue-900
    bg-[url('/public/images/icon-work.svg')] bg-[right_1rem_top] bg-no-repeat">
      <div class="h-12 ">
      </div >
      <div class="grid grid-cols-1 bg-blue-600 p-8 rounded-xl gap-2 lg:gap-4 cursor-pointer hover:bg-blue-300">
        <div class="grid grid-cols-2">
          <div class="text-xl font-bold">  Work  </div>
          <div class="justify-end text-2xl  text-end">... </div>
          <button label="button" class="text-white"></button>
        </div>
        <div class="grid grid-cols-2 lg:grid-cols-1 w-full">
          <div class="text-5xl font-mono"> 5hrs </div>  <!-- daily -->
          <div class="grid place-items-center justify-end lg:justify-start lg:mt-4 text-xl text-blue-200 text-end ">Previous - 7hrs </div>
        </div> <!-- daily -->
      </div>
    </section>

    <section class="card-top bg-blue-100 bg-[url('/public/images/icon-play.svg')] ">
      <div class="h-12 ">
      </div >
      <div class="card-cat">
        <div class="grid grid-cols-2">
          <div class="text-xl font-bold">  Play  </div>
          <div class="justify-end text-2xl  text-end">... </div>
          <button label="button" class="text-white"></button>
        </div>

        <div class="cat-result">
          <div class="text-5xl font-mono"> 23hrs </div>  <!-- daily -->
          <div class="last-result">Previous - 29hrs </div>
        </div>
      </div>
    </section>

    <section class="bg-red-200 rounded-2xl m-4 border-2 border-blue-900
bg-[url('/public/images/icon-study.svg')] bg-[right_1rem_top] bg-no-repeat">
      <div class="h-12 ">
      </div >
      <div class="card-cat">
        <div class="grid grid-cols-2">
          <div class="text-xl font-bold">  Study  </div>
          <div class="justify-end text-2xl  text-end">... </div>
          <button label="button" class="text-white"></button>
        </div>

        <div class="cat-result">
          <div class="text-5xl font-mono"> 10hrs </div>  <!-- daily -->
          <div class="last-result">Previous - 19hrs </div>
        </div>
      </div>
    </section>
    <section class="bg-green-300 rounded-2xl m-4 border-2 border-blue-900
bg-[url('/public/images/icon-exercise.svg')] bg-[right_1rem_top] bg-no-repeat">
      <div class="h-12 ">
      </div >
      <div class="card-cat">
        <div class="grid grid-cols-2">
          <div class="text-xl font-bold">  VueJS  </div>
          <div class="justify-end text-2xl  text-end">... </div>
          <button label="button" class="text-white"></button>
        </div>

        <div class="cat-result">
          <div class="text-5xl font-mono"> 7hrs </div>  <!-- daily -->
          <div class="last-result">Previous - 9hrs </div>
        </div>
      </div>
    </section>
    <section class="bg-violet-300 rounded-2xl m-4 border-2 border-blue-900
bg-[url('/public/images/icon-social.svg')] bg-[right_1rem_top] bg-no-repeat">
      <div class="h-12 ">
      </div >
      <div class="card-cat">
        <div class="grid grid-cols-2">
          <div class="text-xl font-bold">  PostgreSQL  </div>
          <div class="justify-end text-2xl  text-end">... </div>
          <button label="button" class="text-white"></button>
        </div>

        <div class="cat-result">
          <div class="text-5xl font-mono"> 24hrs </div>  <!-- daily -->
          <div class="last-result">Previous - 8hrs </div>
        </div>
      </div>
    </section>
    <section class="bg-orange-300 rounded-2xl m-4 border-2 border-blue-900
bg-[url('/public/images/icon-self-care.svg')] bg-[right_1rem_top] bg-no-repeat">
      <div class="h-12 ">
      </div >
      <div class="card-cat">
        <div class="grid grid-cols-2">
          <div class="text-xl font-bold">  Спорт  </div>
          <div class="justify-end text-2xl  text-end">... </div>
          <button label="button" class="text-white"></button>
        </div>

        <div class="cat-result">
          <div class="text-5xl font-mono"> 1hrs </div>  <!-- daily -->
          <div class="last-result">Previous - 2hrs </div>
        </div>
      </div>
    </section>




  </main>

</template>

<style scoped>

</style>