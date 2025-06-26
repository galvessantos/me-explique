import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TextDisplay } from './text-display';

describe('TextDisplay', () => {
  let component: TextDisplay;
  let fixture: ComponentFixture<TextDisplay>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TextDisplay]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TextDisplay);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
