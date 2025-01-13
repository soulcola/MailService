package ru.javaops.masterjava.persist.model;

import lombok.*;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import ru.javaops.masterjava.persist.model.type.UserFlag;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseEntity {
    @ColumnName("full_name")
    private @NonNull String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    @ColumnName("city_ref")
    private @NonNull String cityRef;

    public User(Integer id, String fullName, String email, UserFlag flag, String cityRef) {
        this(fullName, email, flag, cityRef);
        this.id=id;
    }
}