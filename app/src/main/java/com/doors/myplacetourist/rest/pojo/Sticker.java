package com.doors.myplacetourist.rest.pojo;

import android.graphics.PointF;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class Sticker {

    /**
     * objects_info : [{"placeholder":{"placeholder_id":"data/DB/series/helsinki_3_usadba/images/points","projections":[{"filename":"\"IMG_7817_100.jpg\"","points":[[263,611],[324,612],[325,698],[264,698]]},{"filename":"\"IMG_7817_3.jpg\"","points":[[235,581],[295,582],[297,668],[235,669]]},{"filename":"\"IMG_7817_54.jpg\"","points":[[305,636],[366,636],[367,721],[305,720]]},{"filename":"\"IMG_7818_141.jpg\"","points":[[286,516],[378,515],[383,647],[288,650]]},{"filename":"\"IMG_7818_148.jpg\"","points":[[299,527],[391,526],[396,659],[301,661]]},{"filename":"\"IMG_7818_60.jpg\"","points":[[311,531],[404,530],[410,663],[315,665]]},{"filename":"\"IMG_7818_87.jpg\"","points":[[381,500],[475,499],[478,631],[383,634]]},{"filename":"\"IMG_7819_167.jpg\"","points":[[221,526],[418,526],[427,814],[217,819]]},{"filename":"\"IMG_7819_37.jpg\"","points":[[234,450],[432,450],[442,738],[232,742]]},{"filename":"\"IMG_7819_86.jpg\"","points":[[327,506],[526,504],[535,796],[323,795]]}]},"sticker":{"path":"","sticker_id":"data/DB/series/helsinki_3_usadba/images/points","sticker_text":""}},{"placeholder":{"placeholder_id":"data/DB/series/helsinki_3_usadba/objects/object_1","projections":[{"filename":"\"iosDrsGd_IMG_7819_60_2018-12-13_10-52-40.jpg\"","points":[[118,588],[180,588],[180,515],[118,515]]}]},"sticker":{"path":"http://wikimapia.org/1840328/it/Fiera-del-Levante","sticker_id":"data/DB/series/helsinki_3_usadba/objects/object_1","sticker_text":"Bari\nFiera"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_13","projections":[{"filename":"\"iosDrsGd_IMG_7818_50_2019-01-17_15-07-12.jpg\"","points":[[387,1092],[486,809],[194,1055],[126,809]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_13","sticker_text":"MyGgggg"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_7","projections":[{"filename":"\"iosDrsGd_IMG_7817_0_2019-01-17_12-32-50.jpg\"","points":[[328,686],[474,694],[136,466],[496,466]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_7","sticker_text":"MyPlace Sticker\nHigh"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_6","projections":[{"filename":"\"and01GD_IMG_7817_0.jpg_2018-12-29_03-15-30.jpg\"","points":[[5,280],[363,280],[363,638],[5,638]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_6","sticker_text":"Fhjggh"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_24","projections":[{"filename":"\"IMG_7817_50_2019-01-17_22-52-49.jpg\"","points":[[276,767],[439,962],[275,962],[439,764]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_24","sticker_text":"Iiiiiiiiiiiiiiiiii"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_23","projections":[{"filename":"\"iosUsrGd_IMG_7817_50_2019-01-17_22-51-01.jpg\"","points":[[294,556],[370,627],[294,627],[369,554]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_23","sticker_text":"Uuuuuuuuuuuuuuuuuu"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_12","projections":[{"filename":"\"iosDrsGd_IMG_7818_50_2019-01-17_15-02-32.jpg\"","points":[[94,410],[110,617],[345,634],[384,444]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_12","sticker_text":"Aaaaa"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_8","projections":[{"filename":"\"iosUsrGd_IMG_7817_50_2019-01-17_12-36-08.jpg\"","points":[[288,549],[381,550],[293,623],[388,626]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_8","sticker_text":"Aaaaaaaaaaa"}},{"placeholder":{"placeholder_id":"data/DB/objects/object_9","projections":[{"filename":"\"iosDrsGd_IMG_7817_50_2019-01-17_12-38-29.jpg\"","points":[[453,673],[540,382],[254,433],[281,596]]}]},"sticker":{"path":"","sticker_id":"data/DB/objects/object_9","sticker_text":"Ooooooo\n"}}]
     * scene : /api/render_result/response3681705840156857806_2019-01-28_15-51-37.jpg.result.jpg
     */

    private String scene;
    @JsonAdapter(EmptyStringAsNullTypeAdapter.class)
    private List<ObjectsInfoBean> objects_info;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public List<ObjectsInfoBean> getObjects_info() {
        return objects_info;
    }

    public void setObjects_info(List<ObjectsInfoBean> objects_info) {
        this.objects_info = objects_info;
    }

    public static class Status {
        private Integer code;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;
    }

    public static class ObjectsInfoBean {
        /**
         * placeholder : {"placeholder_id":"data/DB/series/helsinki_3_usadba/images/points","projections":[{"filename":"\"IMG_7817_100.jpg\"","points":[[263,611],[324,612],[325,698],[264,698]]},{"filename":"\"IMG_7817_3.jpg\"","points":[[235,581],[295,582],[297,668],[235,669]]},{"filename":"\"IMG_7817_54.jpg\"","points":[[305,636],[366,636],[367,721],[305,720]]},{"filename":"\"IMG_7818_141.jpg\"","points":[[286,516],[378,515],[383,647],[288,650]]},{"filename":"\"IMG_7818_148.jpg\"","points":[[299,527],[391,526],[396,659],[301,661]]},{"filename":"\"IMG_7818_60.jpg\"","points":[[311,531],[404,530],[410,663],[315,665]]},{"filename":"\"IMG_7818_87.jpg\"","points":[[381,500],[475,499],[478,631],[383,634]]},{"filename":"\"IMG_7819_167.jpg\"","points":[[221,526],[418,526],[427,814],[217,819]]},{"filename":"\"IMG_7819_37.jpg\"","points":[[234,450],[432,450],[442,738],[232,742]]},{"filename":"\"IMG_7819_86.jpg\"","points":[[327,506],[526,504],[535,796],[323,795]]}]}
         * sticker : {"path":"","sticker_id":"data/DB/series/helsinki_3_usadba/images/points","sticker_text":""}
         */
        public ObjectsInfoBean(){}
        public ObjectsInfoBean(@NonNull String stickerName, @NonNull String fileName,
                       String url, @NonNull PointF... points) {

            //set sName and url
            StickerBean stickerBean = new StickerBean();
            stickerBean.setSticker_text(stickerName);
            if(url!=null&&!url.isEmpty()) stickerBean.setPath(url);

            setSticker(stickerBean);

            //set fName and points
            PlaceholderBean placeholderBean = new PlaceholderBean();
            PlaceholderBean.ProjectionsBean projectionsBean =
                    new PlaceholderBean.ProjectionsBean();
            projectionsBean.setFilename(fileName);
            ArrayList<List<Integer>> pointsList = new ArrayList<>();
            for (PointF point : points) {
                pointsList.add(Arrays.asList((int)point.x,(int)point.y));
            }
            projectionsBean.setPoints(pointsList);
            placeholderBean.setProjections(Collections.singletonList(projectionsBean));

            setPlaceholder(placeholderBean);
        }

        private PlaceholderBean placeholder;
        private StickerBean sticker;

        public PlaceholderBean getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(PlaceholderBean placeholder) {
            this.placeholder = placeholder;
        }

        public StickerBean getSticker() {
            return sticker;
        }

        public void setSticker(StickerBean sticker) {
            this.sticker = sticker;
        }

        public static class PlaceholderBean {
            /**
             * placeholder_id : data/DB/series/helsinki_3_usadba/images/points
             * projections : [{"filename":"\"IMG_7817_100.jpg\"","points":[[263,611],[324,612],[325,698],[264,698]]},{"filename":"\"IMG_7817_3.jpg\"","points":[[235,581],[295,582],[297,668],[235,669]]},{"filename":"\"IMG_7817_54.jpg\"","points":[[305,636],[366,636],[367,721],[305,720]]},{"filename":"\"IMG_7818_141.jpg\"","points":[[286,516],[378,515],[383,647],[288,650]]},{"filename":"\"IMG_7818_148.jpg\"","points":[[299,527],[391,526],[396,659],[301,661]]},{"filename":"\"IMG_7818_60.jpg\"","points":[[311,531],[404,530],[410,663],[315,665]]},{"filename":"\"IMG_7818_87.jpg\"","points":[[381,500],[475,499],[478,631],[383,634]]},{"filename":"\"IMG_7819_167.jpg\"","points":[[221,526],[418,526],[427,814],[217,819]]},{"filename":"\"IMG_7819_37.jpg\"","points":[[234,450],[432,450],[442,738],[232,742]]},{"filename":"\"IMG_7819_86.jpg\"","points":[[327,506],[526,504],[535,796],[323,795]]}]
             */

            private String placeholder_id;
            private List<ProjectionsBean> projections;

            public String getPlaceholder_id() {
                return placeholder_id;
            }

            public void setPlaceholder_id(String placeholder_id) {
                this.placeholder_id = placeholder_id;
            }

            public List<ProjectionsBean> getProjections() {
                return projections;
            }

            public void setProjections(List<ProjectionsBean> projections) {
                this.projections = projections;
            }

            public static class ProjectionsBean {
                /**
                 * filename : "IMG_7817_100.jpg"
                 * points : [[263,611],[324,612],[325,698],[264,698]]
                 */

                private String filename;
                private List<List<Integer>> points;

                public String getFilename() {
                    return filename;
                }

                public void setFilename(String filename) {
                    this.filename = filename;
                }

                public List<List<Integer>> getPoints() {
                    return points;
                }

                public void setPoints(List<List<Integer>> points) {
                    this.points = points;
                }
            }
        }

        public static class StickerBean {
            /**
             * path :
             * sticker_id : data/DB/series/helsinki_3_usadba/images/points
             * sticker_text :
             */

            private String Address;
            @SerializedName("Feedback amount")
            private String _$FeedbackAmount52; // FIXME check this code
            @SerializedName("Phone number")
            private String _$PhoneNumber276; // FIXME check this code
            @SerializedName("Price category")
            private String _$PriceCategory101; // FIXME check this code
            private String Rating;
            private String path;
            private String sticker_id;
            private String sticker_text;

            public String getAddress() {
                if(Address!=null) return Address;
                else return "";
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public String get_$FeedbackAmount52() {
                if(_$FeedbackAmount52!=null)
                    return _$FeedbackAmount52;
                else
                    return "";
            }

            public void set_$FeedbackAmount52(String _$FeedbackAmount52) {
                this._$FeedbackAmount52 = _$FeedbackAmount52;
            }

            public String get_$PhoneNumber276() {
                if(_$PhoneNumber276!=null) return _$PhoneNumber276;
                else return "";
            }

            public void set_$PhoneNumber276(String _$PhoneNumber276) {
                this._$PhoneNumber276 = _$PhoneNumber276;
            }

            public String get_$PriceCategory101() {
                if(_$PriceCategory101!=null) return _$PriceCategory101;
                else return "";
            }

            public void set_$PriceCategory101(String _$PriceCategory101) {
                this._$PriceCategory101 = _$PriceCategory101;
            }

            public String getRating() {
                if(Rating!=null)
                    return Rating;
                else
                    return "0";
            }

            public void setRating(String Rating) {
                this.Rating = Rating;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getSticker_id() {
                return sticker_id;
            }

            public void setSticker_id(String sticker_id) {
                this.sticker_id = sticker_id;
            }

            public String getSticker_text() {
                if(sticker_text!=null)
                    return sticker_text;
                else
                    return "";
            }

            public void setSticker_text(String sticker_text) {
                this.sticker_text = sticker_text;
            }
        }
    }

    public String stickerToString() {
        if(getSticker() == null)return "Sticker or Obj info == null!";
       ObjectsInfoBean.StickerBean sticker = getSticker();
       String address = sticker.getAddress();
       String feedback_a = sticker.get_$FeedbackAmount52();
       String phone = sticker.get_$PhoneNumber276();
       String price = sticker.get_$PriceCategory101();
       String rating = sticker.getRating();
       String name = sticker.getSticker_text();

       return "Sticker:{\nName: "+name+" Address:" + address + " Feedback amount: " + feedback_a
               + " Phone: " + phone + " Price: " + price + " Rating: " + rating;
    }

    public ObjectsInfoBean.StickerBean getSticker() {
        if(getObjects_info()!=null&&getObjects_info().size()>0) {
            for(ObjectsInfoBean skB : getObjects_info()) {
                if(skB.getSticker()!=null)
                    return skB.getSticker();
            }
        }
        return null;
    }
}
