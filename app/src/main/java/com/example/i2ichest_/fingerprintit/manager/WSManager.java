package com.example.i2ichest_.fingerprintit.manager;

import android.content.Context;
import android.util.Log;
import com.example.i2ichest_.fingerprintit.R;
import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;
import com.example.i2ichest_.fingerprintit.model.AttendanceModel;
import com.example.i2ichest_.fingerprintit.model.BuildingModel;
import com.example.i2ichest_.fingerprintit.model.FacultyModel;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.MajorModel;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import com.example.i2ichest_.fingerprintit.model.RoomModel;
import com.example.i2ichest_.fingerprintit.model.SectionModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;
import com.example.i2ichest_.fingerprintit.task.WSTask;
import com.example.i2ichest_.fingerprintit.task.WSTaskPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class WSManager {
    private static WSManager wsManager;
    private Context context;
    ParentModel parentModel;
    StudentModel studentModel;
    String splitReceive[]  = null;

    public interface WSManagerListener{
        void onComplete(Object response) ;
        void onError(String error);
    }

    public WSManager(Context context) {
        this.context = context;
    }

    public static WSManager getWsManager(Context context){
        if(wsManager == null)
            wsManager = new WSManager(context);
        return  wsManager;
    }

    public void doLogin(Object object,final WSManagerListener listener){
        if (!(object instanceof LoginModel)){
            return;
        }

        LoginModel loginModel = (LoginModel) object;
        loginModel.toJSONString();

        WSTaskPost taskPost = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Map<String,List<String>> map = new HashMap<String,List<String>>();
                Log.d("response Login", response.toString());

                List<String> listSubject = new ArrayList<>();
                List<String> listLogin = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    JSONArray jsonSubject = jsonObject.getJSONArray("subject");
                    for ( int i = 0 ; i < jsonSubject.length() ; i++){
                        listSubject.add(jsonSubject.get(i).toString());
                    }

                    JSONArray jsonLogin = jsonObject.getJSONArray("login");
                    for ( int i = 0 ; i < jsonLogin.length() ; i++){
                        listLogin.add(jsonLogin.get(i).toString());
                    }

                    map.put("subject",listSubject);
                    map.put("login",listLogin);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Login Complete ", map.toString());
                listener.onComplete(map);

            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("onLoginError " , err.toString());
            }
        });
        taskPost.execute("/login",loginModel.toJSONString());
    }

    public void doSearchStudentParent(Object object,final WSManagerListener listener){
        if(!(object instanceof PersonModel)){
            return;
        }

        PersonModel personModel = (PersonModel) object;
        personModel.toJSONString();

        WSTaskPost taskPost = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                List<StudentModel.Student> listStudent = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        StudentModel studentModel = new StudentModel(jsonArray.get(i).toString());

                        listStudent.add(studentModel.getStudent());
                    }
                    listener.onComplete(listStudent);
                    Log.d("searchStuParentComplete" , response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("searchStuParentErr" , err.toString());
            }
        });
        taskPost.execute("/viewListSubject/searchStudentParent",personModel.toJSONString());
    }

    public void doSearchSubject(Object object,final WSManagerListener listener){
        if(!(object instanceof PersonModel)){
            return;
        }

        PersonModel personModel = (PersonModel) object;
        personModel.toJSONString();

        WSTaskPost taskPost = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("onSearchSubjectComplete" , response.toString());
                final List<SubjectModel> listSubject = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        SubjectModel subject = new SubjectModel(jsonArray.get(i).toString());
                        listSubject.add(subject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onComplete(listSubject);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("onSearchSubjectError" , err.toString());
            }
        });
        taskPost.execute("/viewListSubject",personModel.toJSONString());
    }

    public void doSearchSection(Object object,final WSManagerListener listener){
        if(!(object instanceof SubjectModel)){
            return;
        }

        SubjectModel subjectModel = (SubjectModel) object;
        subjectModel.toJSONString();

        WSTaskPost taskPost = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                SectionModel sectionModel = new SectionModel();

                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    Log.d("SECTION @@@@ :",jsonArray.toString());
                    sectionModel = new SectionModel(jsonArray.get(0).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onComplete(sectionModel.getSection());
                Log.d("onSearchSectionComplete" ,  sectionModel.toString());
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("onSearchSectionError" , err.toString());
            }
        });
        taskPost.execute("/viewListSubject/section",subjectModel.toJSONString());
    }

    public void doSearchPeriod(Object object,final WSManagerListener listener){
        if(!(object instanceof SubjectModel)){
            return;
        }

        SubjectModel subjectModel = (SubjectModel) object;
        subjectModel.toJSONString();

        WSTaskPost taskPost = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
                Log.d("onSearchPeriodComplete" ,  response.toString());
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("onSearchPeriodError" , err.toString());
            }
        });
        taskPost.execute("/viewListSubject/section",subjectModel.toJSONString());
    }

    public void doSearchScheduleDate(Object object,final WSManagerListener listener){
        if(!(object instanceof PeriodModel)){
            return;
        }

        PeriodModel periodModel = (PeriodModel) object;
        periodModel.toJSONString();

        WSTaskPost taskPost = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {

                List<String> listDate = new ArrayList<String>();
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] sp = jsonArray.get(i).toString().split("-");
                        String year = sp[0];
                        String month = sp[1];
                        String day = sp[2];
                        listDate.add(day + "-" + month + "-" + year);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("ScheduleDateComplete" ,  response.toString());
                listener.onComplete(listDate);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("ScheduleDateError" , err.toString());
            }
        });
        taskPost.execute("/annouceNews/searchDate",periodModel.toJSONString());
    }

    public void doAddAnnouceNews(Object object,final WSManagerListener listener){
        if(!(object instanceof AnnouceNewsModel)){
            return;
        }

        AnnouceNewsModel annouceNewsModel = (AnnouceNewsModel) object;
        annouceNewsModel.toJSONString();

        WSTaskPost taskPost = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
                Log.d("AddNewsComplete" ,  response.toString());
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("AddNewsError" , err.toString());
            }
        });
        taskPost.execute("/annouceNews",annouceNewsModel.toJSONString());
    }



    public void verifyParent(Object object,final WSManagerListener listener){
        if(!(object instanceof StudentModel)){
            return;
        }
        studentModel = null;
        studentModel = (StudentModel)object;
        Log.d("studentParent ",studentModel.toJSONString());
        WSTaskPost task = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("response verifyParent ",response);
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.verifyParent), studentModel.toJSONString());
    }


    public void getEnrollment(Object object,final WSManagerListener listener){
        if(!(object instanceof PeriodModel)){
            return;
        }
        PeriodModel period = (PeriodModel)object;

        WSTaskPost task = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                List<AttendanceModel.Attendance> listAttendance = new ArrayList<AttendanceModel.Attendance>();
                Log.d("Attendance response ###",response.toString()+" ####");

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray c = jsonObj.getJSONArray("attendace");
                    for(int x = 0 ; x<c.length() ; x++){
                        JSONObject obj = c.getJSONObject(x);
                        AttendanceModel att = new AttendanceModel(obj.toString());
                        listAttendance.add(att.getAttendance());
                        Log.d("Attendance status ###",att.getAttendance().getStatus()+" ####");
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("##SIZE1 ",listAttendance.size()+" $$$");

                listener.onComplete(listAttendance);


            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.attendance),period.toJSONString());
    }

    public void findTeacher(Object object,final WSManagerListener listener){
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {listener.onError(err);

            }
        });
        task.execute("/searchTeacher?id="+object.toString(),"##");
    }

    public void studentAttendaceForTeacher(String object,final WSManagerListener listener){
        splitReceive = object.toString().split("-");
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute("/attendanceTeacher?section="+splitReceive[0]+"&period="+splitReceive[1],"##");
    }


    public void informLeave(Object object,final WSManagerListener listener){
        if(!(object instanceof InformLeaveModel)){
            return;
        }
        InformLeaveModel informLeaveModel = (InformLeaveModel)object;
        Log.d("informJSON ",informLeaveModel.toJSONString()+"");
        WSTaskPost task = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                try{
                    listener.onComplete(response);
                }catch(Exception s){
                    Log.d(TAG, "onComplete: "+s.getMessage());
                }

            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.informleave),informLeaveModel.toJSONString());
    }

    public void searchInformLeaveForTeacher(Object object,final WSManagerListener listener){

        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                List<InformLeaveModel> listInform = new ArrayList<>();

                try {
                    JSONArray informArray = new JSONArray(response.toString());

                    for(int y=0;y<informArray.length();y++){
                        JSONObject jsonSection = new JSONObject(informArray.get(y).toString());
                        InformLeaveModel informLeaveModel = new InformLeaveModel(jsonSection.toString());
                        listInform.add(informLeaveModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("SIZE inform ",listInform.size()+" ");
                listener.onComplete(listInform);
            }

            @Override
            public void onError(String err) {listener.onError(err);

            }
        });
        task.execute("/listinformleave?id="+object.toString(),"##");
    }


    public void doSearchLeaveHistory (Object object, final WSManagerListener listener) {
        if (!(object instanceof PersonModel)) {
            return;
        }

        PersonModel personModel = (PersonModel) object;
        personModel.toJSONString();

        WSTaskPost task = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                List<InformLeaveModel.InformLeave> listInform = new ArrayList<>();

                try {
                    JSONArray informArray = new JSONArray(response.toString());

                    for (int y = 0; y < informArray.length(); y++) {
                        JSONObject jsonSection = new JSONObject(informArray.get(y).toString());
                        InformLeaveModel informLeaveModel = new InformLeaveModel(jsonSection.toString());
                        listInform.add(informLeaveModel.getInformLeave());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("SIZE inform ", listInform.size() + " ");
                listener.onComplete(listInform);

            }

            @Override
            public void onError(String err) {
                listener.onError(err);

            }
        });
        task.execute("/leaveHistory",personModel.toJSONString());
    }

    public void getAnnounceNewsFromStudentId(Object object,final WSManagerListener listener){
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                List<AnnouceNewsModel.AnnouceNews> listAnnounce = new ArrayList<AnnouceNewsModel.AnnouceNews>();
                try {
                    JSONArray announce = new JSONArray(response.toString());
                    for (int i = 0; i < announce.length(); i++) {
                        JSONObject json = new JSONObject(announce.get(i).toString());
                        AnnouceNewsModel an = new AnnouceNewsModel(json.toString());
                        Calendar cal = Calendar.getInstance();
                        long parseDate = Long.parseLong(an.getAnnouceNews().getSchedule().getScheduleDate());
                        Date dd = new Date(parseDate);
                        cal.setTime(dd);
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        String date = year  + "-" + month + "-" + day;
                        an.getAnnouceNews().getSchedule().setScheduleDate(date);
                        listAnnounce.add(an.getAnnouceNews());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onComplete: "+response.toString());
                listener.onComplete(listAnnounce);
            }

            @Override
            public void onError(String err) {listener.onError(err);
                listener.onError(err);
                Log.d("searchHistory Error" , err.toString());
            }
        });
        task.execute("/viewAnnouceNews?studentId="+object.toString(),"##");
    }

    public void updateAttendanceStatus(Object object,final WSManagerListener listener){
        if(!(object instanceof InformLeaveModel.InformLeave)){
            return;
        }
        InformLeaveModel inform =  new InformLeaveModel();
        inform.setInformLeave((InformLeaveModel.InformLeave)object);

        WSTaskPost task = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);

            }
            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("searchHistory Error" , err.toString());
            }
        });
        task.execute("/updateInformStatus",inform.toJSONString());
    }

    public void doSearchDateOfInformLeave(Object object,final WSManagerListener listener){
        if(!(object instanceof PeriodModel)){
            return;
        }
        PeriodModel periodModel = (PeriodModel) object;
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                //Log.d("ScheduleDateComplete" ,  response.toString());
                List<String> date = new ArrayList<String>();
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        date.add(jsonArray.get(i).toString());
                    }

                    listener.onComplete(date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("ScheduleDateError" , err.toString());
            }
        });
        task.execute("/informLeaveSearchDate?id="+periodModel.getPeriod().getPeriodID(),"##");
    }
}

