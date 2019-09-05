package ru.otus.erinary.ms.dataserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.erinary.ms.dataserver.dao.AddressModel;
import ru.otus.erinary.ms.dataserver.dao.PhoneModel;
import ru.otus.erinary.ms.dataserver.dao.UserModel;
import ru.otus.erinary.ms.dataserver.orm.DBService;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataServer {

    private final DBService<UserModel> userDaoDBService;

    public void start() {
      log.info("DataServer started");
      userDaoDBService.create(new UserModel("john", 23, new AddressModel("qwer"), Set.of(new PhoneModel("123"))));
        System.out.println(userDaoDBService.load(1));
    }

}
