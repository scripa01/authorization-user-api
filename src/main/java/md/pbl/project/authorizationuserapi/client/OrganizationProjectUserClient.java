package md.pbl.project.authorizationuserapi.client;

import md.pbl.project.authorizationuserapi.model.dto.CreateUserDto;
import md.pbl.project.authorizationuserapi.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "org-service", url = "${org.service.url}")
public interface OrganizationProjectUserClient {
    @PostMapping("/api/core/organizations/{orgId}/users")
    UserDto createUser(
            @PathVariable("orgId") Long orgId,
            @RequestBody CreateUserDto dto
    );

    @GetMapping("/api/core/organizations/{orgId}/users/findUserByUsername")
    List<UserDto> findByUsername(
            @PathVariable("orgId") Long orgId,
            @RequestParam("username") String username
    );
}
