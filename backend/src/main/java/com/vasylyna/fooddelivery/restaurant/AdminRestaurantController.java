package com.vasylyna.fooddelivery.restaurant;
import com.vasylyna.fooddelivery.common.ResourceNotFoundException;import jakarta.validation.Valid;import java.math.BigDecimal;import java.util.List;
import org.springframework.http.HttpStatus;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin/restaurants") public class AdminRestaurantController{
 private final RestaurantRepository repo;public AdminRestaurantController(RestaurantRepository repo){this.repo=repo;}
 @GetMapping public List<RestaurantDtos.RestaurantResponse> all(){return repo.findAll().stream().map(RestaurantDtos.RestaurantResponse::from).toList();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public RestaurantDtos.RestaurantResponse create(@Valid @RequestBody RestaurantDtos.RestaurantRequest r){return RestaurantDtos.RestaurantResponse.from(repo.save(new Restaurant(r.name(),r.description(),r.cuisine(),rating(r.rating()),r.openingHours(),r.imageUrl(),r.open()==null||r.open())));}
 @PutMapping("/{id}") public RestaurantDtos.RestaurantResponse update(@PathVariable Long id,@Valid @RequestBody RestaurantDtos.RestaurantRequest r){Restaurant v=repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Restaurant was not found"));v.update(r.name(),r.description(),r.cuisine(),rating(r.rating()),r.openingHours(),r.imageUrl(),r.open()==null||r.open());return RestaurantDtos.RestaurantResponse.from(repo.save(v));}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){if(!repo.existsById(id))throw new ResourceNotFoundException("Restaurant was not found");repo.deleteById(id);}
 private BigDecimal rating(BigDecimal value){return value==null?BigDecimal.ZERO:value;}
}
