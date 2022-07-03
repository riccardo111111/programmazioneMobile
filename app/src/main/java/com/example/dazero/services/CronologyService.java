package com.example.dazero.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.dazero.db.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CronologyService extends Service {

    ResultService resultService;
    int idUser;

    public CronologyService(ResultService resultService, int idUser) {
        this.resultService = resultService;
        this.idUser = idUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    public ArrayList<Result> showAllResults() {
        Log.d("das", "id user " + idUser);
        if (resultService.getResultByID(idUser) == null) {
            return null;
        }
        return resultService.getResultByID(idUser);
    }

    public ArrayList<Result> showResultOfTheMonth() {
        ArrayList<Result> array = resultService.getResultByID(idUser);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();

        if (array == null) {
            return null;
        } else {
            for (int i = 0; i < array.size(); i++) {
                try {
                    if (!isWithinRange(
                            new SimpleDateFormat("yyyy-MM-dd").parse(array.get(i).date),
                            result,
                            new Date())
                    ) {
                        array.remove(i);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return array;
    }

    public ArrayList<Result> showResultOfTheWeek() {
        ArrayList<Result> array = resultService.getResultByID(idUser);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, -1);
        Date result = cal.getTime();

        if (array == null) {
            return null;
        } else {
            for (int i = 0; i < array.size(); i++) {

                try {
                    if (!isWithinRange(
                            new SimpleDateFormat("yyyy-MM-dd").parse(array.get(i).date),
                            result,
                            new Date())
                    ) {
                        array.remove(i);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return array;
    }

    boolean isWithinRange(Date testDate, Date startDate, Date endDate) {
        return !(testDate.before(startDate) || testDate.after(endDate));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}