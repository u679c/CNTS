package com.cnts.system.domain;

import java.io.Serializable;

public class TicketAssignDTO implements Serializable
{
    private Long ticketId;
    private Long adminId;

    public Long getTicketId()
    {
        return ticketId;
    }

    public void setTicketId(Long ticketId)
    {
        this.ticketId = ticketId;
    }

    public Long getAdminId()
    {
        return adminId;
    }

    public void setAdminId(Long adminId)
    {
        this.adminId = adminId;
    }
}
