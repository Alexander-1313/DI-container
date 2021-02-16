package com.elinext.dao.provider;

import com.elinext.container.Provider;
import com.elinext.context.ApplicationContext;
import com.elinext.dao.EventDao;

public class EventDaoProvider implements Provider<EventDao> {

    ApplicationContext context = ApplicationContext.getInstance();

    @Override
    public EventDao getInstance() {
        return (EventDao) context.getContainerObjByClass(EventDao.class);
    }
}
