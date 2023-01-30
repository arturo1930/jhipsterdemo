/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TareaDetailComponent from '@/entities/tarea/tarea-details.vue';
import TareaClass from '@/entities/tarea/tarea-details.component';
import TareaService from '@/entities/tarea/tarea.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Tarea Management Detail Component', () => {
    let wrapper: Wrapper<TareaClass>;
    let comp: TareaClass;
    let tareaServiceStub: SinonStubbedInstance<TareaService>;

    beforeEach(() => {
      tareaServiceStub = sinon.createStubInstance<TareaService>(TareaService);

      wrapper = shallowMount<TareaClass>(TareaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { tareaService: () => tareaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTarea = { id: 123 };
        tareaServiceStub.find.resolves(foundTarea);

        // WHEN
        comp.retrieveTarea(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tarea).toBe(foundTarea);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTarea = { id: 123 };
        tareaServiceStub.find.resolves(foundTarea);

        // WHEN
        comp.beforeRouteEnter({ params: { tareaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tarea).toBe(foundTarea);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
