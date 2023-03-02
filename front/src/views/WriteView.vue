<script setup lang="ts">
import {ref} from "vue";
import axios from 'axios';

const title = ref("");
const content = ref("");

const write = function () {
  axios.post("http://localhost:8080/posts", {
    title: title.value,
    content: content.value
  })
      .catch(function (error) {
        if (error.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          console.log(error.response.data);
          console.log(error.response.status);
          console.log(error.response.headers);
        } else if (error.request) {
          // The request was made but no response was received
          // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
          // http.ClientRequest in node.js
          console.log(error.request);
        } else {
          // Something happened in setting up the request that triggered an Error
          console.log('Error', error.message);
        }
        console.log(error.config);
      });
};
</script>

<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"/>
  </div>
  <div class="mt-2">
    <el-input v-model="content" type="textarea" row="15"/>
  </div>
  <div class="mt-2">
    <el-button type="primary" @click="write()">글 작성완료</el-button>
  </div>
</template>

<style>
</style>