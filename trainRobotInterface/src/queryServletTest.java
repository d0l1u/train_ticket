import com.l9e.common.InitConfigServlet;
import com.l9e.service.SysSettingServiceImpl;
import com.l9e.servlet.MobileQueryTicketServlet;
import com.l9e.servlet.QueryTicketServlet;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class queryServletTest {

    private QueryTicketServlet servlet;

    private HttpServletRequest mockRequest;

    private HttpServletResponse mockRespones;

    private ServletContext mockServletContext;

    private RequestDispatcher mockDispathcher;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        InitConfigServlet init = new InitConfigServlet();
        init.init();
        SysSettingServiceImpl sysSettingServiceImpl = new SysSettingServiceImpl();


        try {
            int s = sysSettingServiceImpl.querySysVal("queryLeftTicket");
        } catch (RepeatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(sysSettingServiceImpl.getSysVal());


    }

    @Before
    public void setUp() throws Exception {
        mockRequest = EasyMock.createStrictMock(HttpServletRequest.class);
        mockRespones = EasyMock.createStrictMock(HttpServletResponse.class);
        mockServletContext = EasyMock.createStrictMock(ServletContext.class);
        mockDispathcher = EasyMock.createStrictMock(RequestDispatcher.class);
        servlet = new QueryTicketServlet();
        servlet.init();
        InitConfigServlet init = new InitConfigServlet();
        init.init();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {

        long a = System.currentTimeMillis();

        mockRequest.setCharacterEncoding("utf-8");
        mockRespones.setCharacterEncoding("utf-8");

        EasyMock.expect(mockRequest.getParameter("channel")).andStubReturn("19e");
        EasyMock.expect(mockRequest.getParameter("type")).andStubReturn("yupiao");
        EasyMock.expect(mockRequest.getParameter("train_code")).andStubReturn("");
        EasyMock.expect(mockRequest.getParameter("from_station")).andStubReturn("北京");
        EasyMock.expect(mockRequest.getParameter("arrive_station")).andStubReturn("上海");
        EasyMock.expect(mockRequest.getParameter("travel_time")).andStubReturn("2018-05-29");
        EasyMock.expect(mockRequest.getParameter("purpose_codes")).andStubReturn("ADULT");
        EasyMock.expect(mockRequest.getParameter("check_spare_num")).andStubReturn("");
        EasyMock.expect(mockRequest.getParameter("alter_refund")).andStubReturn("");


        EasyMock.replay(mockRequest);
        EasyMock.replay(mockRespones);

        servlet.doGet(mockRequest, mockRespones);

        long b = System.currentTimeMillis();

        System.out.println((b - a) / 1000 + "秒");


    }

}
