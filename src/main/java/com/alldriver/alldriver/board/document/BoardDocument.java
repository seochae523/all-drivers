//package com.alldriver.alldriver.board.document;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.DateFormat;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.util.Date;
//import java.util.List;
//
//@Builder
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Document(indexName = "board")
//public class BoardDocument {
//    @Id
//    @Field(type = FieldType.Long)
//    private Long id;
//
//    @Field(type = FieldType.Text)
//    private String category;
//
//    @Field(type = FieldType.Text)
//    private String title;
//
//    @Field(type = FieldType.Text)
//    private String content;
//
//    @Field(type = FieldType.Text)
//    private String companyLocation;
//
//    @Field(type = FieldType.Date, format = DateFormat.year_month_day)
//    private Date startAt;
//
//    @Field(type = FieldType.Date, format = DateFormat.year_month_day)
//    private Date endAt;
//
//    @Field(type = FieldType.Text)
//    private List<String> cars;
//
//    @Field(type = FieldType.Text)
//    private List<String> jobs;
//
//    @Field(type = FieldType.Text)
//    private List<String> locations;
//
//
//}
