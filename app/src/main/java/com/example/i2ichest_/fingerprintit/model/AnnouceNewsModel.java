package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AnnouceNewsModel {
    private AnnouceNews annouceNews;
    Gson gson = new GsonBuilder().create();

    public AnnouceNewsModel(){
        annouceNews = new AnnouceNews();
    }

    public AnnouceNewsModel(String jsonResponse){
        annouceNews = gson.fromJson(jsonResponse,AnnouceNews.class);
    }

    public AnnouceNews getEnrollment(){
        return annouceNews;
    }

    public String toJSONString(){
        return gson.toJson(this.annouceNews);
    }

    public class AnnouceNews {
        private int annouceNewsID;
        private String annouceNewsType;
        private String detail;
        TeacherModel.Teacher teacher;
        ScheduleModel.Schedule schedule;

        public AnnouceNews() {
        }

        public AnnouceNews(int annouceNewsID, String annouceNewsType, String detail) {
            this.annouceNewsID = annouceNewsID;
            this.annouceNewsType = annouceNewsType;
            this.detail = detail;
        }

        public int getAnnouceNewsID() {
            return annouceNewsID;
        }

        public void setAnnouceNewsID(int annouceNewsID) {
            this.annouceNewsID = annouceNewsID;
        }

        public String getAnnouceNewsType() {
            return annouceNewsType;
        }

        public void setAnnouceNewsType(String annouceNewsType) {
            this.annouceNewsType = annouceNewsType;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public TeacherModel.Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(TeacherModel.Teacher teacher) {
            this.teacher = teacher;
        }

        public ScheduleModel.Schedule getSchedule() {
            return schedule;
        }

        public void setSchedule(ScheduleModel.Schedule schedule) {
            this.schedule = schedule;
        }
    }
}
