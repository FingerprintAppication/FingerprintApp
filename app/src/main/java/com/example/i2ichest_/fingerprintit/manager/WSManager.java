package com.example.i2ichest_.fingerprintit.manager;

import android.content.Context;
import android.util.Log;
import com.example.i2ichest_.fingerprintit.R;
import com.example.i2ichest_.fingerprintit.model.AttendanceModel;
import com.example.i2ichest_.fingerprintit.model.EnrollmentModel;
import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;
import com.example.i2ichest_.fingerprintit.task.WSTask;
import com.example.i2ichest_.fingerprintit.task.WSTaskPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                listener.onComplete(response);
                Log.d("onLoginComplete " , response.toString());
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
                listener.onComplete(response);
                Log.d("searchStuParentComplete" , response.toString());
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
                listener.onComplete(response);
                Log.d("onSearchSubjectComplete" , response.toString());
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
                Log.d("onSearchSubjectError" , err.toString());
            }
        });
        taskPost.execute("/viewListSubject",personModel.toJSONString());
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
        taskPost.execute("/viewListSubject/period",subjectModel.toJSONString());
    }

    public void doVerifyStudentParent(Object object,final WSManagerListener listener){
        if(!(object instanceof StudentModel)){
            return;
        }
        studentModel = (StudentModel)object;
        WSTaskPost task = new WSTaskPost(this.context, new WSTaskPost.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                    try {
                        JSONObject job = new JSONObject(response.toString());
                        Log.d("job ",job.toString());
                        if(!job.get("personID").toString().equals("0")) {
                            job.remove("fingerprintData");
                            StudentModel studentModel = new StudentModel(job.toString());
                            listener.onComplete(studentModel);
                        }else{
                            listener.onComplete("ไม่พบรหัสนักศึกษาในฐานข้อมูล");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.verify_student_parent),studentModel.toJSONString());
    }

    public void verifyParent(Object object,final WSManagerListener listener){
        if(!(object instanceof ParentModel)){
            return;
        }
        parentModel = (ParentModel)object;

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
        task.execute(context.getString(R.string.verifyParent), parentModel.toJSONString());
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
                    for(int x=0;x<c.length();x++){
                        JSONObject obj = c.getJSONObject(x);
                        AttendanceModel att = new AttendanceModel();
                        Long attendanceID = Long.parseLong(obj.getString("attendanceID"));
                        String status = obj.getString("status");
                        String statusDescription = obj.getString("statusDescription");
                        att.getAttendance().setAttendanceID(attendanceID);
                        att.getAttendance().setStatus(status);
                        att.getAttendance().setStatusDescription(statusDescription);
                        listAttendance.add(att.getAttendance());
                        Log.d("Attendance status ###",status+" ####");
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

}
