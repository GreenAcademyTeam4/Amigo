package com.example.amigo_project.dto.chat;

import lombok.Data;

import java.util.Objects;

@Data
public class RoomDataDTO {

    public int schoolId;
    public int grade;
    public int classRoom;

    public boolean equals (RoomDataDTO dto) {

        if(this.schoolId == dto.getSchoolId() && this.grade == dto.getGrade() && this.classRoom == dto.getClassRoom()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schoolId,grade,classRoom);
    }
}
