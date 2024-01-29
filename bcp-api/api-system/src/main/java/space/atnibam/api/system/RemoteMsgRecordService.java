package space.atnibam.api.system;

import space.atnibam.api.system.interceptor.SystemFeignInterceptor;
import space.atnibam.common.core.domain.LocalMessageRecord;
import space.atnibam.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(value = "modules-system",configuration = SystemFeignInterceptor.class)
public interface RemoteMsgRecordService {
    @PostMapping("/msg-record")
    public R saveMsgRecord(@RequestBody LocalMessageRecord localMessageRecord);

    @PutMapping("/msg-record")
    public R updateMsgRecord(@RequestBody LocalMessageRecord localMessageRecord);

    @GetMapping("/msg-record/fail-record")
    public R<List<LocalMessageRecord>> queryFileStateMsgRecord();
}
