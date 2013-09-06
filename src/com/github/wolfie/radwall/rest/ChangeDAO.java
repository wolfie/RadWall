package com.github.wolfie.radwall.rest;

import com.google.gson.annotations.SerializedName;

public class ChangeDAO {
    public String kind;
    public String id;
    public String project;
    public String branch;

    @SerializedName("change_id")
    public String changeId;

    public String subject;
    public String status;
    public String created;
    public String updated;
    public boolean mergeable;
    public String _sortkey;
    public String _number;
    public OwnerDAO owner;

    @Override
    public String toString() {
        return "ChangeDAO [id=" + id + ", subject=" + subject + "]";
    }

}
