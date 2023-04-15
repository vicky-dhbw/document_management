export class DocumentMetadata {
  title: string = '';
  filename: string = '';
  creator: string = '';
  dateOfCreation: string = '';
  filesize: number = 0;
  tags: string[] = [];

  gettitle(): string {
    return this.title;
  }

  settitle(value: string) {
    this.title = value;
  }

  getfilename(): string {
    return this.filename;
  }

  setfilename(value: string) {
    this.filename = value;
  }

  getcreator(): string {
    return this.creator;
  }

  setcreator(value: string) {
    this.creator = value;
  }

  getdateOfCreation(): string {
    return this.dateOfCreation;
  }

  setdateOfCreation(value: string) {
    this.dateOfCreation = value;
  }

  getfilesize(): number {
    return this.filesize;
  }

  setfilesize(value: number) {
    this.filesize = value;
  }

  gettags(): string[] {
    return this.tags;
  }

  settags(value: string[]) {
    this.tags = value;
  }
}