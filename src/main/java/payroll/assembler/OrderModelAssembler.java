package payroll.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import payroll.model.Status;
import payroll.controller.OrderController;
import payroll.model.Order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order order) {

        EntityModel<Order> orderModel = new EntityModel<>(order,
                linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders")
        );

        // Conditional links based on state of the order

        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(
                    linkTo(methodOn(OrderController.class)
                            .cancel(order.getId())).withRel("cancel"));
            orderModel.add(
                    linkTo(methodOn(OrderController.class)
                            .complete(order.getId())).withRel("complete"));
        }

        return orderModel;
    }

}
