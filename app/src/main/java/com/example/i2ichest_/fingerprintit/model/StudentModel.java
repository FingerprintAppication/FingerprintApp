package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StudentModel extends PersonModel{
    private Student student;

    Gson gson = new GsonBuilder().create();

    public StudentModel(){
        student = new Student();
    }

    public StudentModel(String jsonResponse){
        student = gson.fromJson(jsonResponse,Student.class);
    }

    public Student getStudent(){
        return student;
    }

    public String toJSONString(){
        return gson.toJson(this.student);
    }

    public class Student extends Person{
        private Long studentID;
        private String  encode;
        private String parentPhone;
        ParentModel.Parent parent;

        public Student() {
        }

        public Student(Long studentID, String encode, String parentPhone,Long personID, String title, String firstName, String lastName,String fingerprintData) {
            super(personID,title, firstName, lastName, fingerprintData);
            this.studentID = studentID;
            this.encode = encode;
            this.parentPhone = parentPhone;
        }

        public ParentModel.Parent getParent() {
            return parent;
        }

        public void setParent(ParentModel.Parent parent) {
            this.parent = parent;
        }

        public Long getStudentID() {
            return studentID;
        }

        public void setStudentID(Long studentID) {
            this.studentID = studentID;
        }

        public String getEncode() {
            return encode;
        }

        public void setEncode(String encode) {
            this.encode = encode;
        }

        public String getParentPhone() {
            return parentPhone;
        }

        public void setParentPhone(String parentPhone) {
            this.parentPhone = parentPhone;
        }

        public String checkStudentIdAndPhone(String inputPhone,String databasePhone){
            String resultCheck = "";
            if(databasePhone == null){
                databasePhone = "null value";
            }
            if(!databasePhone.equalsIgnoreCase(inputPhone) ){
                resultCheck = "เบอร์โทรศัพท์ไม่ตรงตามฐานข้อมูล";
            }

            return resultCheck;
        }
    }


}
