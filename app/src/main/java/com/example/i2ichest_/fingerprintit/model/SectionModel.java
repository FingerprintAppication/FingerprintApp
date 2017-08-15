package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MSI on 5/7/2560.
 */

public class SectionModel implements Serializable {
    private Section section;
    Gson gson = new GsonBuilder().create();

    public SectionModel(){
        section = new Section();
    }

    public SectionModel(String jsonResponse){
        section = gson.fromJson(jsonResponse,Section.class);
    }

    public Section getSection(){
        return section;
    }

    public String toJSONString(){
        return gson.toJson(this.section);
    }

    public class Section implements Serializable{
        private Long sectionID;
        private int schoolYear;
        private int sectionNumber;
        private int semester;
        SubjectModel.Subject subject;
        List<PeriodModel.Period> periodList;
        TeacherModel.Teacher teacher;

        public Section(){}

        public Section(Long sectionID, int schoolYear, int sectionNumber, int semester, SubjectModel.Subject subject) {
            this.sectionID = sectionID;
            this.schoolYear = schoolYear;
            this.sectionNumber = sectionNumber;
            this.semester = semester;
            this.subject = subject;
        }

        public SubjectModel.Subject getSubject() {
            return subject;
        }

        public List<PeriodModel.Period> getPeriodList() {
            return periodList;
        }

        public void setPeriodList(List<PeriodModel.Period> periodList) {
            this.periodList = periodList;
        }

        public TeacherModel.Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(TeacherModel.Teacher teacher) {
            this.teacher = teacher;
        }

        public void setSubject(SubjectModel.Subject subject) {
            this.subject = subject;
        }

        public int getSemester() {
            return semester;
        }

        public void setSemester(int semester) {
            this.semester = semester;
        }

        public int getSectionNumber() {
            return sectionNumber;
        }

        public void setSectionNumber(int sectionNumber) {
            this.sectionNumber = sectionNumber;
        }

        public int getSchoolYear() {
            return schoolYear;
        }

        public void setSchoolYear(int schoolYear) {
            this.schoolYear = schoolYear;
        }

        public Long getSectionID() {
            return sectionID;
        }

        public void setSectionID(Long sectionID) {
            this.sectionID = sectionID;
        }
    }
}
