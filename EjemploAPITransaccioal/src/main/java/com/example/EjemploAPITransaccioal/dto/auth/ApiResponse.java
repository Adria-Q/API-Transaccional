package com.example.EjemploAPITransaccioal.dto.auth;

    public class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        // Constructor
        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public T getData() { return data; }
    }

