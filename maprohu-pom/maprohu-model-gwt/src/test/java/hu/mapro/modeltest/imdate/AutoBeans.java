
package hu.mapro.modeltest.imdate;

import java.util.List;

public final class AutoBeans {


    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.AndFilter.class)
    public interface AndFilter extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.MultiFilter
    {


        List<AutoBeans.UserFilter> getFilters();

        void setFilters(List<AutoBeans.UserFilter> filters);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.Area.class)
    public interface Area extends com.google.web.bindery.requestfactory.shared.ValueProxy
    {


        java.lang.String getName();

        void setName(java.lang.String name);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.BoundingBox.class)
    public interface BoundingBox extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.Area
    {


        java.lang.Double getLatitude1();

        void setLatitude1(java.lang.Double latitude1);

        java.lang.Double getLatitude2();

        void setLatitude2(java.lang.Double latitude2);

        java.lang.Double getLongitude1();

        void setLongitude1(java.lang.Double longitude1);

        java.lang.Double getLongitude2();

        void setLongitude2(java.lang.Double longitude2);

        java.lang.String getName();

        void setName(java.lang.String name);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.Composite.class)
    public interface Composite extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.Area
    {


        List<AutoBeans.Area> getAreas();

        void setAreas(List<AutoBeans.Area> areas);

        java.lang.String getName();

        void setName(java.lang.String name);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.Coordinate.class)
    public interface Coordinate extends com.google.web.bindery.requestfactory.shared.ValueProxy
    {


        java.lang.Double getLatitude();

        void setLatitude(java.lang.Double latitude);

        java.lang.Double getLongitude();

        void setLongitude(java.lang.Double longitude);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.Criteria.class)
    public interface Criteria extends com.google.web.bindery.requestfactory.shared.ValueProxy
    {


        java.lang.String getCache();

        void setCache(java.lang.String cache);

        java.lang.String getMain();

        void setMain(java.lang.String main);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.MultiFilter.class)
    public interface MultiFilter extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.UserFilter
    {


        List<AutoBeans.UserFilter> getFilters();

        void setFilters(List<AutoBeans.UserFilter> filters);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.NotFilter.class)
    public interface NotFilter extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.UserFilter
    {


        AutoBeans.UserFilter getFilter();

        void setFilter(AutoBeans.UserFilter filter);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.OrFilter.class)
    public interface OrFilter extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.MultiFilter
    {


        List<AutoBeans.UserFilter> getFilters();

        void setFilters(List<AutoBeans.UserFilter> filters);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.Polygon.class)
    public interface Polygon extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.Area
    {


        List<AutoBeans.Coordinate> getCoordinates();

        void setCoordinates(List<AutoBeans.Coordinate> coordinates);

        java.lang.String getName();

        void setName(java.lang.String name);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.Sql2Filter.class)
    public interface Sql2Filter extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.UserFilter
    {


        java.lang.String getCacheSql();

        void setCacheSql(java.lang.String cacheSql);

        java.lang.String getMainSql();

        void setMainSql(java.lang.String mainSql);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.SqlFilter.class)
    public interface SqlFilter extends com.google.web.bindery.requestfactory.shared.ValueProxy, AutoBeans.UserFilter
    {


        java.lang.String getSql();

        void setSql(java.lang.String sql);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.User.class)
    public interface User extends com.google.web.bindery.requestfactory.shared.ValueProxy
    {


        java.lang.String getCountry();

        void setCountry(java.lang.String country);

        java.lang.String getName();

        void setName(java.lang.String name);

        java.lang.String getOrganization();

        void setOrganization(java.lang.String organization);

        java.lang.String getPassword();

        void setPassword(java.lang.String password);

        java.lang.String getSurname();

        void setSurname(java.lang.String surname);

        java.lang.String getUsername();

        void setUsername(java.lang.String username);

        java.lang.String getWhereCache();

        void setWhereCache(java.lang.String whereCache);

        java.lang.String getWhereMain();

        void setWhereMain(java.lang.String whereMain);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.UserArea.class)
    public interface UserArea extends com.google.web.bindery.requestfactory.shared.ValueProxy
    {


        AutoBeans.UserFilter getFilter();

        void setFilter(AutoBeans.UserFilter filter);

        java.lang.String getUsername();

        void setUsername(java.lang.String username);

    }

    @com.google.web.bindery.requestfactory.shared.ProxyFor(hu.mapro.modeltest.imdate.ref.UserFilter.class)
    public interface UserFilter extends com.google.web.bindery.requestfactory.shared.ValueProxy
    {


    }

}
