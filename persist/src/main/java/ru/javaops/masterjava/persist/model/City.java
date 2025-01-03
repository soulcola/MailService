package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class City extends BaseEntity {

    private @NonNull String ref;
    private @NonNull String name;

    public City(Integer id, String name, String ref) {
        this(name, ref);
        this.id = id;
    }
}
