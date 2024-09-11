import { Buffer } from 'buffer';
import { Injectable, Provider } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class Base64EncodeDecode {
  constructor() { }

  decode(base64Data: string): string {
    if (this.isBase64(base64Data)) {
      // create a buffer from the base64 string using Buffer library
      const buffer = Buffer.from(base64Data, 'base64');
      // return the buffer converted to string
      return buffer.toString('utf-8');
    } else {
      throw new Error('Invalid base64 string');
    }
  }

  encode(someString: string): string {
    // create a buffer from the string using Buffer library
    const buffer = Buffer.from(someString, 'utf-8');
    // return the buffer converted to string base64 encoded
    return buffer.toString('base64');
  }

  isBase64(str: string): boolean {
    const base64Regex = /^(?:[A-Za-z0-9+/]{4})*?(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$/;
    return base64Regex.test(str);
  }
}