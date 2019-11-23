package com.pis.gmall.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class UmsMember implements Serializable{

    @Id
    //主键返回策略
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String member_level_id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String status;
    private String create_time;
    private String icon;
    private String gender;
    private String birthday;
    private String city;
    private String job;
    private String personalized_signature;
    private String source_type;
    private String integration;
    private String growth;
    private String luckey_count;
    private String history_integration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_level_id() {
        return member_level_id;
    }

    public void setMember_level_id(String member_level_id) {
        this.member_level_id = member_level_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPersonalized_signature() {
        return personalized_signature;
    }

    public void setPersonalized_signature(String personalized_signature) {
        this.personalized_signature = personalized_signature;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public String getLuckey_count() {
        return luckey_count;
    }

    public void setLuckey_count(String luckey_count) {
        this.luckey_count = luckey_count;
    }

    public String getHistory_integration() {
        return history_integration;
    }

    public void setHistory_integration(String history_integration) {
        this.history_integration = history_integration;
    }
}
