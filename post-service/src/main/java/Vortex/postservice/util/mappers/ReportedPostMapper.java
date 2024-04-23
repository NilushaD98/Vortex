package Vortex.postservice.util.mappers;

import Vortex.postservice.collection.ReportedPost;
import Vortex.postservice.dto.request.PostReportDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportedPostMapper {
    ReportedPost DTOToEntity(PostReportDTO postReportDTO);
}
