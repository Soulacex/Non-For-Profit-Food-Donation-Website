package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.HelperMessage.HelperMessage;

public interface HelperMessageDAO {

HelperMessage creatHelperMessage(HelperMessage helperMessage) throws IOException;

HelperMessage searchMessage(int Id) throws IOException;

HelperMessage[] getAllHelperMessages() throws IOException;
    
}
