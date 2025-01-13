package ru.javaops.masterjava.persist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
    @NonNull @ColumnName("user_id") private Integer userId;
    @NonNull @ColumnName("group_id") private Integer groupId;
}