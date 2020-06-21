package com.example.deepclass.myutil;

public class QuestionBean {
    private int id;
    private String qstTitle;
    private String qstDisp;

    public int getId(){
        return id;
    }

    public String getQstTitle() {
        return qstTitle;
    }

    public String getQstDesp() {
        return qstDisp;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setQstTitle(String qstTitle) {
        this.qstTitle = qstTitle;
    }


    public void setQstDesp(String qstDisp) {
        this.qstDisp = qstDisp;
    }

}
