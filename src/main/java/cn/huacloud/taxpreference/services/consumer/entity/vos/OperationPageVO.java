package cn.huacloud.taxpreference.services.consumer.entity.vos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class OperationPageVO<T> {
    private LocalDate date;
    private List<T> pageVOList;
}
