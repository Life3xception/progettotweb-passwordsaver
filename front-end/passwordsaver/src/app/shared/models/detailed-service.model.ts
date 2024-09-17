import { ServiceI as ServiceI } from "./service.model";

export interface DetailedServiceI extends ServiceI {
    serviceTypeName: string;
}