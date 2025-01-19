package ru.javaops.web;

import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.javaops.masterjava.ExceptionType;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@XmlType(namespace = "http://common.javaops.ru/")
public class FaultInfo {
    private @NonNull ExceptionType type;

    @Override
    public String toString() {
        return type.toString();
    }
}
