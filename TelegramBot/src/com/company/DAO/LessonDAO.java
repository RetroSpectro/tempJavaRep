package com.company.DAO;

import com.company.model.*;
import com.google.inject.internal.asm.$ClassTooLargeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LessonDAO {
    private Connection connection;

    public LessonDAO(Connection connection) {
        setConnection(connection);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void addLesson(Lesson lesson) {
        try {

            String sql_str = "INSERT INTO bot_db.bot_schema.lessons(name,time,room, week_type, day) VALUES(?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql_str);
            ps.setString(1, lesson.getName());
            ps.setString(2, lesson.getTime());
            ps.setString(3, lesson.getRoom());
            ps.setString(4, lesson.getWeekType().toString());
            ps.setString(5, lesson.getDay().getName());
            ps.execute();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public int getLessonId(Lesson lesson) {
        try {
            String sql_str = "SELECT * FROM  bot_db.bot_schema.lessons WHERE name =? AND time =? AND room= ? AND week_type = ? AND day = ? ";
            PreparedStatement ps = connection.prepareStatement(sql_str);
            ps.setString(1, lesson.getName());
            ps.setString(2, lesson.getTime());
            ps.setString(3, lesson.getRoom());
            ps.setString(4, lesson.getWeekType().toString());
            ps.setString(5, lesson.getDay().getName());
            ResultSet res = ps.executeQuery();
            int lesson_id=-1;
            while (res.next())
            {
             lesson_id  = res.getInt(1);
            }
            return lesson_id;
        }
        catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return -1;
        }

    }

    public void addLesson(Lesson lesson, int  group_id) {
        addLesson(lesson);
        try {
            int lesson_id = getLessonId(lesson);
            if (lesson_id != -1) {
                String sql_str = "INSERT INTO bot_db.bot_schema.group_lesson(group_id,lesson_id) VALUES(?,?)";
                PreparedStatement ps = connection.prepareStatement(sql_str);
                ps.setInt(1, group_id);
                ps.setInt(2, lesson_id);
                ps.execute();
            }
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public void updateLesson(Lesson lesson, int id) {
        try {
            String sql_str = "UPDATE bot_db.bot_schema.lessons(name,time,room,week_type,day) VALUES(?,?,?,?,?) WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql_str);
            ps.setString(1, lesson.getName());
            ps.setString(2, lesson.getTime());
            ps.setString(3, lesson.getRoom());
            ps.setString(4, lesson.getWeekType().toString());
            ps.setString(5, lesson.getDay().getName());
            ps.setInt(4, id);
            ps.execute();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }


     public ArrayList<Lesson> getAllLessons(String user_name)
     {
         try{
             ArrayList<Lesson> lessons = new ArrayList<>();
             int groupId = new GroupDAO(connection).getGroupId(user_name);
             ArrayList<Integer> lessons_id = getAllLessonsById(groupId);
             if(lessons_id!=null && lessons_id.size()>0)
             {
                 for (int i = 0; i < lessons_id.size(); i++) {
                     String sql_str = "SELECT * FROM  bot_db.bot_schema.lessons WHERE  id = ? ";
                     PreparedStatement ps = connection.prepareStatement(sql_str);
                     ps.setInt(1, lessons_id.get(i));
                     ResultSet res = ps.executeQuery();
                     while (res.next())
                     {
                            WeekType weekType;
                            if(res.getString(5).equals("even"))
                            {
                                weekType = WeekType.even;
                            }
                            else if(res.getString(5).equals("odd"))
                            {
                                weekType = WeekType.odd;
                            }
                            else{
                                weekType = WeekType.none;
                            }
                         lessons.add(new Lesson(res.getString(2), res.getString(3), res.getString(4), weekType, new Day(res.getString(6))));
                         res.getInt(1);
                     }
                 }
             }

            return lessons;
         } catch (SQLException e) {
             System.out.println("Connection Failed");
             e.printStackTrace();
             return null;
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }

     private ArrayList<Integer> getAllLessonsById(int groupId)
     {
         try{
             ArrayList<Integer> lessons_id =new ArrayList<>();
             String sql_str = "SELECT * FROM  bot_db.bot_schema.group_lesson WHERE  group_id = ? ";
             PreparedStatement ps = connection.prepareStatement(sql_str);
             ps.setInt(1, groupId);

             ResultSet res = ps.executeQuery();
             while (res.next())
             {
                 lessons_id.add(res.getInt(1));
             }
             return lessons_id;
         } catch (SQLException e) {
             System.out.println("Connection Failed");
             e.printStackTrace();
             return null;
         }
     }

}
